package it.polito.wa2.ecommerce.orderservice.client.item

import it.polito.wa2.ecommerce.orderservice.client.item.ItemDTO
import java.math.BigDecimal

class PurchaseItemDTO(
    productId: String,
    amount: Int,
    val price: BigDecimal
    ): ItemDTO(productId, amount)

// TODO(add validation)
