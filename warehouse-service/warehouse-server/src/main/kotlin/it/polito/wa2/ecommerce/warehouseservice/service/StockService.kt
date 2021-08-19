package it.polito.wa2.ecommerce.warehouseservice.service

import it.polito.wa2.ecommerce.warehouseservice.client.StockDTO

interface StockService {
    fun getStocksByWarehouseID(warehouseId: Long): StockDTO
    fun getStockByWarehouseIDandProductID(warehouseId: Long, productID: Long): StockDTO
    fun addStock(stock: StockDTO): StockDTO
}