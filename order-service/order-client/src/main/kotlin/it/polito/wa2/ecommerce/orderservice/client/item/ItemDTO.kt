package it.polito.wa2.ecommerce.orderservice.client.item

import javax.validation.constraints.Min
import javax.validation.constraints.NotNull


open class ItemDTO(
    @field:NotNull
    val productId: String,
    @field:NotNull
    @field:Min(1)
    val amount: Int
)
