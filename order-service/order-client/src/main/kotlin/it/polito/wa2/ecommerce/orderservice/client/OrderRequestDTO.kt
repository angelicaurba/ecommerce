package it.polito.wa2.ecommerce.orderservice.client

data class OrderRequestDTO<T:ItemDTO>(
//    val buyerId: Long, TODO lo mettiamo o lo leggiamo dal cookie? cookie
    val buyerWalletId: Long,
    val address: String,
    val deliveryItems: List<T>
)
