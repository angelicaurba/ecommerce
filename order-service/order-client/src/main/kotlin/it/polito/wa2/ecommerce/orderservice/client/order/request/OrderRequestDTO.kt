package it.polito.wa2.ecommerce.orderservice.client.order.request

import it.polito.wa2.ecommerce.orderservice.client.item.ItemDTO

data class OrderRequestDTO<T: ItemDTO>(
//    val buyerId: Long, TODO lo mettiamo o lo leggiamo dal cookie? cookie
    val buyerWalletId: String,
    val address: String,
    val deliveryItems: List<T>
)

// TODO(add validation)