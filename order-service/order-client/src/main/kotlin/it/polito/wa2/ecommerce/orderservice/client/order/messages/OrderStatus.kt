package it.polito.wa2.ecommerce.orderservice.client.order.messages

import it.polito.wa2.ecommerce.common.saga.domain.Emittable
import javax.validation.constraints.NotNull

//Message to be delivered once the order processing has been completed

//TODO change to OrderStatusDTO
data class OrderStatus(
    @field:NotNull
    val orderID: String,
    @field:NotNull
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
