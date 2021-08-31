package it.polito.wa2.ecommerce.orderservice.client

import it.polito.wa2.ecommerce.orderservice.client.order.response.Status
import org.jetbrains.annotations.NotNull

data class UpdateOrderRequestDTO(
    @field:NotNull
    val status: Status
)
