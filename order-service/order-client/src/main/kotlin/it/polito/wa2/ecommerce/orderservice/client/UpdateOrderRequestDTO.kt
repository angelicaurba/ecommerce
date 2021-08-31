package it.polito.wa2.ecommerce.orderservice.client

// TODO check import
import it.polito.wa2.ecommerce.orderservice.client.order.messages.ResponseStatus

data class UpdateOrderRequestDTO(

    val responseStatus: ResponseStatus
)

// TODO(add validation)
