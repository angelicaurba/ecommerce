package it.polito.wa2.ecommerce.warehouseservice.service

import it.polito.wa2.ecommerce.orderservice.client.item.ItemDTO
import it.polito.wa2.ecommerce.orderservice.client.item.PurchaseItemDTO
import it.polito.wa2.ecommerce.orderservice.client.order.messages.ProductWarehouseDTO
import it.polito.wa2.ecommerce.orderservice.client.order.request.ItemsInWarehouseDTO
import it.polito.wa2.ecommerce.walletservice.client.transaction.request.OrderTransactionRequestDTO
import it.polito.wa2.ecommerce.warehouseservice.client.StockDTO
import it.polito.wa2.ecommerce.warehouseservice.client.StockRequestDTO

interface StockService {
    fun updateStockFields(warehouseId: String, productID: String, stockRequestDTO: StockRequestDTO): StockDTO
    fun updateOrCreateStock(warehouseId: String, productID: String, stockRequestDTO: StockRequestDTO): StockDTO
    fun addStock(warehouseID: String, stockRequest: StockRequestDTO): StockDTO
    fun getStockByWarehouseIDandProductID(warehouseID: String, productID: String): StockDTO
    fun getStocksByWarehouseID(warehouseID: String, pageIdx: Int, pageSize: Int): List<StockDTO>
    fun deleteStockByWarehouseIdAndProductId(warehouseId: String, productID: String)

    fun getWarehouseHavingProducts(productList: List<PurchaseItemDTO>): List<ProductWarehouseDTO>
    fun updateAndRetrieveAmount(productList: List<PurchaseItemDTO>): List<OrderTransactionRequestDTO>
    fun cancelRequestUpdate(productList: List<ItemsInWarehouseDTO<ItemDTO>>)

}
