package it.polito.wa2.ecommerce.orderservice.client.order.messages

import it.polito.wa2.ecommerce.common.saga.domain.Emittable
import org.jetbrains.annotations.NotNull

// message sent from warehouse after updating warehouses stocks

class OrderDetailsDTO(
    @NotNull
    val orderId: String,
    @NotNull
    val productWarehouseList: List<ProductWarehouseDTO>
): Emittable {
    override fun getId(): String {
        return orderId
    }
}