package it.polito.wa2.ecommerce.orderservice.client

// TODO check import
import it.polito.wa2.ecommerce.orderservice.client.order.messages.Status

data class UpdateOrderRequestDTO(

    val status: Status
)

// TODO(add validation)
