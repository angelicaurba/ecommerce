package it.polito.wa2.ecommerce.orderservice.domain

import it.polito.wa2.ecommerce.common.EntityBase
import java.math.BigDecimal
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.validation.constraints.DecimalMin
import javax.persistence.Table
import javax.persistence.UniqueConstraint

import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["order_id", "product_id"])])
class PurchaseItem (
    @Column(nullable = false) @field:NotNull
    var productId: String,
    @Column(nullable = false) @field:Min(1) @field:NotNull
    var amount: Int,
    @Column(nullable = false) @field:NotNull
    @field:DecimalMin(value = "0.00", inclusive = true, message = "amount should not be negative")
    var price: BigDecimal,
    @Column(nullable = true)
    var warehouseId: String?,

) : EntityBase<Long>(){
    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id")
    var order: Order? = null
}