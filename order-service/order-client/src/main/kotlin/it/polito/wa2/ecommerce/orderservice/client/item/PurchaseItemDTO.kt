package it.polito.wa2.ecommerce.orderservice.client.item

import java.math.BigDecimal
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.Digits
import javax.validation.constraints.NotNull

class PurchaseItemDTO(
    productId: String,
    amount: Int,
    @field:DecimalMin("0.00", inclusive= true)
    @field:Digits(fraction=2, integer = 10)
    @field:NotNull
    val price: BigDecimal
    ): ItemDTO(productId, amount)

