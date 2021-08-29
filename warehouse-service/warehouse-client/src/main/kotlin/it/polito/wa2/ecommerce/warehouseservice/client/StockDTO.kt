package it.polito.wa2.ecommerce.warehouseservice.client

data class StockDTO(
    val stockID: String,
    val warehouseID: String,
    val productID: String,
    val quantity: Long,
    val alarm: Long
)