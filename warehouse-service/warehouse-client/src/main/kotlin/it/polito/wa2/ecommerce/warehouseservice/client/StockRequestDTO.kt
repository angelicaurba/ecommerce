package it.polito.wa2.ecommerce.warehouseservice.client

import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

data class StockRequestDTO(
    @field:NotNull
    val warehouseID: String? = null,
    @field:NotNull
    val productID: String? = null,
    @field:NotNull @field:Min(0)
    val quantity: Long? = null,
    @field:NotNull @field:Min(0)
    val alarm: Long? = null
)