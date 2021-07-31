package it.polito.wa2.ecommerce.orderservice.client

data class OrderRequestDTO(
//    val buyerId: Long, TODO lo mettiamo o lo leggiamo dal cookie? cookie
//    val address: String, TODO lo mettiamo nel cookie o nell'ordine?
    val buyerWalletId: Long,
    val deliveryItems: List<DeliveryItemDTO>
)
