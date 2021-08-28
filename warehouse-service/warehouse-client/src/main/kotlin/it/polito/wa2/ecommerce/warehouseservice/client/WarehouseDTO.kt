package it.polito.wa2.ecommerce.warehouseservice.client

// TODO check constraint
data class WarehouseDTO(
    val id: String,
    val name: String,
    val address: String,
    val adminID: String
)