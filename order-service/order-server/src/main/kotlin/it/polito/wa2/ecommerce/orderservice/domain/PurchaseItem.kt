package it.polito.wa2.ecommerce.orderservice.domain

import it.polito.wa2.ecommerce.common.EntityBase
import it.polito.wa2.ecommerce.orderservice.client.item.PurchaseItemDTO
import java.math.BigDecimal
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.persistence.UniqueConstraint
import javax.validation.constraints.*

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["order_id", "product_id"])])
class PurchaseItem (
    @Column(nullable = false, name="product_id") @field:NotNull
    @field:Size(min = 1, max = 26)
    var productId: String,
    @Column(nullable = false) @field:Min(1) @field:NotNull
    var amount: Int,
    @Column(nullable = false) @field:NotNull
    @field:Digits(fraction=2, integer = 10)
    @field:DecimalMin(value = "0.00", inclusive = true, message = "amount should not be negative")
    var price: BigDecimal,
    @Column(nullable = true)
    var warehouseId: String?,

) : EntityBase<Long>(){
    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id")
    var order: Order? = null

    fun toDTO(): PurchaseItemDTO{
        return PurchaseItemDTO(productId, amount, price)
    }
}

fun PurchaseItemDTO.toEntity(): PurchaseItem{
    return PurchaseItem(
        productId,
        amount,
        price,
        null
    )
}