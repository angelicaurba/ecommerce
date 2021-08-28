package it.polito.wa2.ecommerce.warehouseservice.client

import javax.validation.constraints.NotNull

data class WarehouseRequestDTO (
    @field:NotNull
    val name: String? = null,
    @field:NotNull
    val address: String? = null,
    @field:NotNull
    val adminID: String? = null,
)