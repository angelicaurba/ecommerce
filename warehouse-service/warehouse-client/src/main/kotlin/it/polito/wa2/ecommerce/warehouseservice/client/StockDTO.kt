package it.polito.wa2.ecommerce.warehouseservice.client

import org.jetbrains.annotations.NotNull

data class StockDTO(
    val warehouseID: Long?,
    val productID: String,
    val quantity: Long,
    val alarm: Long
)