package it.polito.wa2.ecommerce.orderservice.domain

import it.polito.wa2.ecommerce.common.EntityBase
import it.polito.wa2.ecommerce.orderservice.client.item.PurchaseItemDTO
import it.polito.wa2.ecommerce.orderservice.client.order.request.ItemsInWarehouseDTO
import it.polito.wa2.ecommerce.orderservice.client.order.response.OrderDTO
import it.polito.wa2.ecommerce.orderservice.client.order.response.Status

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.OneToMany
import javax.persistence.Table
import javax.validation.constraints.NotNull

@Table(name = "orders")
@Entity
class Order(
    @Column @field:NotNull
    val buyerId: String,
    @Column @field:NotNull
    val address: String,
    @Column @field:NotNull
    val buyerWalletId: String,
    @field:NotNull @OneToMany(mappedBy = "order")
    val deliveryItems: Set<PurchaseItem>,
    @Column @field:NotNull
    val status: Status
) : EntityBase<Long>() {
    fun toDTO(): OrderDTO{
        val itemsInWarehouseDTOList: List<ItemsInWarehouseDTO<PurchaseItemDTO>>

        val warehouseItemsMap : Map<String, List<PurchaseItemDTO>> =
        deliveryItems.groupBy ({it -> it.warehouseId!!}, {it -> PurchaseItemDTO(it.productId, it.amount, it.price)})

        return OrderDTO(
            buyerId,
            address,
            buyerWalletId,
            warehouseItemsMap
                .toList()
                .map {
                    ItemsInWarehouseDTO<PurchaseItemDTO>(
                        it.first,
                        it.second
                    )
                },
            status
        )
    }
}
