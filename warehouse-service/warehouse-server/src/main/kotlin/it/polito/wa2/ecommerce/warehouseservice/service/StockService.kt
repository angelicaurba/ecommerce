package it.polito.wa2.ecommerce.warehouseservice.service

import it.polito.wa2.ecommerce.warehouseservice.client.StockDTO
import it.polito.wa2.ecommerce.warehouseservice.client.StockRequestDTO

interface StockService {
    fun updateStockFields(warehouseId: String, productID: String, stockRequestDTO: StockRequestDTO): StockDTO
    fun updateOrCreateStock(warehouseId: String, productID: String, stockRequestDTO: StockRequestDTO): StockDTO
    fun addStock(warehouseID: String, stockRequest: StockRequestDTO): StockDTO
    fun getStockByWarehouseIDandProductID(warehouseID: String, productID: String): StockDTO
    fun getStocksByWarehouseID(warehouseID: String, pageIdx: Int, pageSize: Int): List<StockDTO>
    fun deleteStockByWarehouseIdAndProductId(warehouseId: String, productID: String)
}
