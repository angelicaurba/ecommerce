package it.polito.wa2.ecommerce.orderservice.domain

import it.polito.wa2.ecommerce.common.EntityBase
import it.polito.wa2.ecommerce.common.exceptions.ForbiddenException
import it.polito.wa2.ecommerce.orderservice.client.item.PurchaseItemDTO
import it.polito.wa2.ecommerce.orderservice.client.order.request.ItemsInWarehouseDTO
import it.polito.wa2.ecommerce.orderservice.client.order.response.OrderDTO
import it.polito.wa2.ecommerce.orderservice.client.order.response.Status
import it.polito.wa2.ecommerce.orderservice.utils.extractProductInWarehouse
import javax.persistence.*

import javax.validation.constraints.NotNull

@Table(name = "orders")
@Entity
class Order(
    @Column @field:NotNull
    var buyerId: String,
    @Column @field:NotNull
    var address: String,
    @Column @field:NotNull
    var buyerWalletId: String,

    @Column @field:NotNull
    var status: Status

) : EntityBase<Long>() {

    @field:NotNull @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
    var deliveryItems: MutableSet<PurchaseItem> = mutableSetOf()
    fun toDTO(): OrderDTO{
        return OrderDTO(
            getId().toString(),
            buyerId,
            address,
            buyerWalletId,
            deliveryItems.extractProductInWarehouse { PurchaseItemDTO(it.productId, it.amount, it.price) },
            status
        )
    }

    fun updateStatus(newStatus: Status, e:Exception?=null){
        val exception = e ?: ForbiddenException( "Cannot update status from $status to $newStatus")

        when(newStatus){
            Status.PENDING -> {
                throw exception
            }
            Status.ISSUED -> if(status != Status.PENDING) throw exception
            Status.DELIVERING -> if(status != Status.ISSUED) throw exception
            Status.DELIVERED -> if(status != Status.DELIVERING) throw exception
            Status.FAILED -> if(status != Status.FAILED) throw exception
            Status.CANCELED -> if(status != Status.ISSUED) throw exception
        }

        status = newStatus
    }
}
