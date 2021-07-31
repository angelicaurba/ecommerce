package it.polito.wa2.ecommerce.orderservice.client

data class OrderDTO(
    val buyerId: String,
    val address: String,
    val buyerWalletId: Long,
    val deliveryItems: List<DeliveryItemDTO>,
    val status: Status
)

enum class Status{
    PENDING, ISSUED, DELIVERED, FAILED, CANCELED
}