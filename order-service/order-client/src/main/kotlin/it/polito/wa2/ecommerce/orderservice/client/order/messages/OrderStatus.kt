package it.polito.wa2.ecommerce.orderservice.client.order.messages

import it.polito.wa2.ecommerce.common.saga.domain.Emittable

//Message to be delivered once the order processing has been completed

//TODO change to OrderStatusDTO
data class OrderStatus(
    val orderID: String,
    val responseStatus: ResponseStatus,
    val errorMessage: String?
):Emittable {
    override fun getId(): String {
        return orderID
    }
}

enum class ResponseStatus{
    COMPLETED, // The transaction has been correctly executed
    FAILED // The transaction was not correctly executed
}

enum class EventTypeOrderStatus{
    OrderOk,
    OrderPaymentFailed,
    OrderItemsNotAvailable
}
