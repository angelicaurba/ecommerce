package it.polito.wa2.ecommerce.warehouseservice.client

import javax.validation.constraints.NotNull

data class StockRequestDTO(
    @field:NotNull
    val warehouseID: String? = null,
    @field:NotNull
    val productID: String? = null,
    @field:NotNull
    val quantity: Long? = null,
    @field:NotNull
    val alarm: Long? = null
)