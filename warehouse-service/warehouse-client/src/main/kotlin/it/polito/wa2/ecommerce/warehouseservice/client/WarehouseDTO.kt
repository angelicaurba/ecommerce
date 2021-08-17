package it.polito.wa2.ecommerce.warehouseservice.client

import org.jetbrains.annotations.NotNull

data class WarehouseDTO(
    val id: Long?,
    @field:NotNull
    val name: String,
    @field:NotNull
    val address: String,
    val adminID: String
)