package it.polito.wa2.ecommerce.orderservice.client.order.response

import it.polito.wa2.ecommerce.orderservice.client.item.PurchaseItemDTO
import it.polito.wa2.ecommerce.orderservice.client.order.request.ItemsInWarehouseDTO

data class OrderDTO(
    val buyerId: String,
    val address: String,
    val buyerWalletId: String,
    val deliveryItems: List<ItemsInWarehouseDTO<PurchaseItemDTO>>,
    val status: Status
)

enum class Status{
    PENDING, ISSUED, DELIVERING, DELIVERED, FAILED, CANCELED
}

// TODO(add validation)