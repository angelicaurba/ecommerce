package it.polito.wa2.ecommerce.orderservice.client

import java.math.BigDecimal

class PurchaseItemDTO(
    productId: String,
    amount: Int,
    val price: BigDecimal
    ): ItemDTO(productId, amount)
