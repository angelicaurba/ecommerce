package it.polito.wa2.ecommerce.warehouseservice.service.impl

import it.polito.wa2.ecommerce.common.getPageable
import it.polito.wa2.ecommerce.orderservice.client.item.ItemDTO
import it.polito.wa2.ecommerce.orderservice.client.item.PurchaseItemDTO
import it.polito.wa2.ecommerce.orderservice.client.order.messages.ProductWarehouseDTO
import it.polito.wa2.ecommerce.orderservice.client.order.request.ItemsInWarehouseDTO
import it.polito.wa2.ecommerce.walletservice.client.transaction.request.OrderTransactionRequestDTO
import it.polito.wa2.ecommerce.warehouseservice.client.StockDTO
import it.polito.wa2.ecommerce.warehouseservice.client.StockRequestDTO
import it.polito.wa2.ecommerce.warehouseservice.client.WarehouseDTO
import it.polito.wa2.ecommerce.warehouseservice.domain.Stock
import it.polito.wa2.ecommerce.warehouseservice.domain.Warehouse
import it.polito.wa2.ecommerce.warehouseservice.exception.StockNotFound
import it.polito.wa2.ecommerce.warehouseservice.repository.StockRepository
import it.polito.wa2.ecommerce.warehouseservice.service.StockService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class StockServiceImpl: StockService {

    @Autowired lateinit var stockRepository: StockRepository

    @Autowired lateinit var warehouseService: WarehouseServiceImpl

    private fun sendNotification(stock: Stock) {
        // TODO call mail service to send email
    }

    private fun updateStockQuantity(stock: Stock, newQuantity: Long): Stock {
        stock.quantity = newQuantity
        if (newQuantity <= stock.alarm)
            sendNotification(stock)
        return stock
    }

    override fun updateStockFields(warehouseId: String, productID: String, stockRequestDTO: StockRequestDTO): StockDTO {
        // TODO check product should exist
        var stock = getStockOrThrowException(warehouseId,productID)

        stockRequestDTO.warehouseID?.also {
            val warehouse = warehouseService.getWarehouseOrThrowException(stockRequestDTO.warehouseID!!)
            stock.warehouse = warehouse }
        stockRequestDTO.productID?.also { stock.product = it }
        stockRequestDTO.alarm?.also { stock.alarm = it }
        stockRequestDTO.quantity?.also { it ->  stock = updateStockQuantity(stock,it) }

        return stockRepository.save(stock).toDTO()
    }

    override fun updateOrCreateStock(
        warehouseId: String,
        productID: String,
        stockRequestDTO: StockRequestDTO
    ): StockDTO {
        // TODO check product should exist
        var warehouse = warehouseService.getWarehouseOrThrowException(warehouseId)
        var stock = stockRepository.findByWarehouseAndProduct(warehouse,productID)

        if ( stock != null ){

            if (warehouseId != stockRequestDTO.warehouseID){
                warehouse = warehouseService.getWarehouseOrThrowException(stockRequestDTO.warehouseID!!)
                stock.warehouse = warehouse
            }

            stock.product = stockRequestDTO.productID!!
            stock.alarm = stockRequestDTO.alarm!!

            // stock.quantity = stockRequestDTO.quantity!!
            stock = updateStockQuantity(stock,stockRequestDTO.quantity!!)

            return stockRepository.save(stock).toDTO()
        } else
            return addStock(warehouseId, stockRequestDTO)
    }


    override fun addStock(warehouseID: String, stockRequest: StockRequestDTO): StockDTO {
        val warehouse = warehouseService.getWarehouseOrThrowException(stockRequest.warehouseID!!)
        // TODO check product id must exist, quantity should be higher than alarm when adding a stock?
        val newStock = Stock(
            warehouse,
            stockRequest.productID!!,
            stockRequest.quantity!!,
            stockRequest.alarm!!
        )

        return stockRepository.save(newStock).toDTO()
    }

    override fun getStockByWarehouseIDandProductID(warehouseID: String, productID: String): StockDTO {
        val stock = getStockOrThrowException(warehouseID,productID)

        return stock.toDTO()
    }

    override fun getStocksByWarehouseID(warehouseID: String, pageIdx: Int, pageSize: Int): List<StockDTO> {
        val page = getPageable(pageIdx, pageSize)
        val warehouse = warehouseService.getWarehouseOrThrowException(warehouseID)

        return stockRepository.findAllByWarehouse(warehouse,page).toList().map { it.toDTO() }
    }

    override fun deleteStockByWarehouseIdAndProductId(warehouseId: String, productID: String) {
        val stock = getStockOrThrowException(warehouseId,productID)
        stockRepository.delete(stock)
    }

    override fun getAllWarehousesHavingProduct(productID: String, pageIdx: Int, pageSize: Int): List<WarehouseDTO> {
        val page = getPageable(pageIdx, pageSize)
        // return warehouseRepository.
        // TODO page missing for controller and i need ALL for orderService"
        return stockRepository.findAllByProduct(productID).map { it -> it.warehouse }.toList().map { it.toDTO() }
    }

    override fun getWarehouseHavingProducts(productList: List<PurchaseItemDTO>): List<ProductWarehouseDTO> {
        val list = mutableListOf<ProductWarehouseDTO>()

        // TODO the price is for each item

        productList.forEach{
            val stocks = stockRepository.findAllByProductAndQuantityIsGreaterThanEqual(it.productId, it.amount)
            if (stocks.isEmpty())
                throw  Exception("Items not available") // TODO define exception
            list.add(ProductWarehouseDTO(stocks.first().warehouse.getId()!!,stocks.first().product))
        }

        return list
    }

    override fun updateAndRetrieveAmount(productList: List<PurchaseItemDTO>): List<OrderTransactionRequestDTO> {

        // TODO raggruppare in un'unica transazione i prodotti dallo stesso warehouse

        val list = mutableListOf<OrderTransactionRequestDTO>()

        productList.forEach{
            val stocks = stockRepository.findAllByProductAndQuantityIsGreaterThanEqual(it.productId, it.amount)
            if (stocks.isEmpty())
                throw  Exception("Items not available") // TODO define exception
            val chosenStock = stocks.first() // TODO ordinare per (disponibilità-alarm) e prendere il primo
            updateStockQuantity(chosenStock, chosenStock.quantity - it.amount)
            list.add(OrderTransactionRequestDTO(chosenStock.warehouse.getId()!!,it.price))
        }

        return list
    }

    override fun cancelRequestUpdate(productList: List<ItemsInWarehouseDTO<ItemDTO>>) {

        productList.forEach{ itemsInWarehouse ->
            itemsInWarehouse.purchaseItems.forEach{ item ->
                val stock = getStockOrThrowException(itemsInWarehouse.warehouseId!!, item.productId)
                updateStockQuantity(stock, stock.quantity + item.amount)
            }
        }
    }


    private fun getStockOrThrowException(warehouseId: String, productID: String) : Stock {
        val warehouse = warehouseService.getWarehouseOrThrowException(warehouseId)

        return stockRepository.findByWarehouseAndProduct(warehouse,productID) ?: throw StockNotFound(warehouseId,productID)
    }

}