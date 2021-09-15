package it.polito.wa2.ecommerce.orderservice.client.order.messages

import javax.validation.constraints.NotNull


data class ProductWarehouseDTO(
    @field:NotNull
    val warehouseId: String,
    @field:NotNull
    val productId: String
) {
}