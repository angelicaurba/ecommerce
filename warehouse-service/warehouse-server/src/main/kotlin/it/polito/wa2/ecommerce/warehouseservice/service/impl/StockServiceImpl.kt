package it.polito.wa2.ecommerce.warehouseservice.service.impl

import it.polito.wa2.ecommerce.common.constants.mailTopic
import it.polito.wa2.ecommerce.common.exceptions.BadRequestException
import it.polito.wa2.ecommerce.common.getPageable
import it.polito.wa2.ecommerce.common.parseID
import it.polito.wa2.ecommerce.common.saga.service.MessageService
import it.polito.wa2.ecommerce.mailservice.client.MailDTO
import it.polito.wa2.ecommerce.orderservice.client.item.ItemDTO
import it.polito.wa2.ecommerce.orderservice.client.item.PurchaseItemDTO
import it.polito.wa2.ecommerce.orderservice.client.order.messages.ProductWarehouseDTO
import it.polito.wa2.ecommerce.orderservice.client.order.request.ItemsInWarehouseDTO
import it.polito.wa2.ecommerce.walletservice.client.transaction.request.OrderTransactionRequestDTO
import it.polito.wa2.ecommerce.warehouseservice.client.StockDTO
import it.polito.wa2.ecommerce.warehouseservice.client.StockRequestDTO
import it.polito.wa2.ecommerce.warehouseservice.client.WarehouseDTO
import it.polito.wa2.ecommerce.warehouseservice.domain.Stock
import it.polito.wa2.ecommerce.warehouseservice.exception.ItemsNotAvailable
import it.polito.wa2.ecommerce.warehouseservice.exception.StockNotFound
import it.polito.wa2.ecommerce.warehouseservice.repository.StockRepository
import it.polito.wa2.ecommerce.warehouseservice.service.StockService
import it.polito.wa2.ecommerce.warehouseservice.service.WarehouseService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
class StockServiceImpl: StockService {

    @Autowired lateinit var stockRepository: StockRepository

    @Autowired lateinit var warehouseService: WarehouseService

    @Autowired lateinit var messageService: MessageService

    private fun sendNotification(stock: Stock) {

        val mailSubject = "Alarm notification"

        val mailBody = "Hi admin,\n" +
                "in the warehouse ${stock.warehouse.name} " +
                "the quantity of a product ${stock.product} is below the alarm level."

        val message = MailDTO(
            stock.warehouse.adminID,
            null,
            mailSubject,
            mailBody
        )

        messageService.publish(
            message,
            "SendEmail",
            mailTopic
        )
    }

    private fun updateStockQuantity(stock: Stock, newQuantity: Long): Stock {
        stock.quantity = newQuantity
        if (newQuantity <= stock.alarm)
            sendNotification(stock)
        return stock
    }

    @PreAuthorize("hasAuthority(T(it.polito.wa2.ecommerce.common.Rolename).ADMIN)")
    override fun updateStockFields(warehouseId: String, productID: String, stockRequestDTO: StockRequestDTO): StockDTO {

        if ( (stockRequestDTO.warehouseID != null && warehouseId != stockRequestDTO.warehouseID)  ||
             (stockRequestDTO.productID != null && productID != stockRequestDTO.productID)  )
            throw BadRequestException("WarehouseId or productId contains error")

        warehouseService.isAuthorized(warehouseService.getWarehouseOrThrowException(warehouseId).adminID)

        var stock = getStockOrThrowException(warehouseId,productID)
/*
        stockRequestDTO.warehouseID?.also {
            val warehouse = warehouseService.getWarehouseOrThrowException(stockRequestDTO.warehouseID!!)
            stock.warehouse = warehouse }
        stockRequestDTO.productID?.also { stock.product = it }
*/

        stockRequestDTO.alarm?.also { stock.alarm = it }
        stockRequestDTO.quantity?.also { stock.quantity = it }

        if (stock.quantity <= stock.alarm)
            sendNotification(stock)

        return stockRepository.save(stock).toDTO()
    }

    @PreAuthorize("hasAuthority(T(it.polito.wa2.ecommerce.common.Rolename).ADMIN)")
    override fun updateOrCreateStock(
        warehouseId: String,
        productID: String,
        stockRequestDTO: StockRequestDTO
    ): StockDTO {
        if ( warehouseId != stockRequestDTO.warehouseID || productID != stockRequestDTO.productID )
            throw BadRequestException("WarehouseId or productId contains error")

        var warehouse = warehouseService.getWarehouseOrThrowException(warehouseId)

        warehouseService.isAuthorized(warehouse.adminID)

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

    @PreAuthorize("hasAuthority(T(it.polito.wa2.ecommerce.common.Rolename).ADMIN)")
    override fun addStock(warehouseID: String, stockRequest: StockRequestDTO): StockDTO {


        val warehouse = warehouseService.getWarehouseOrThrowException(stockRequest.warehouseID!!)
        warehouseService.isAuthorized(warehouse.adminID)

        if ( warehouseID != stockRequest.warehouseID )
            throw BadRequestException("WarehouseId contains error")

        val newStock = Stock(
            warehouse,
            stockRequest.productID!!,
            stockRequest.quantity!!,
            stockRequest.alarm!!
        )

        if (newStock.quantity < newStock.alarm)
            sendNotification(newStock)

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

    @PreAuthorize("hasAuthority(T(it.polito.wa2.ecommerce.common.Rolename).ADMIN)")
    override fun deleteStockByWarehouseIdAndProductId(warehouseId: String, productID: String) {
        warehouseService.isAuthorized(warehouseService.getWarehouseOrThrowException(warehouseId).adminID)
        val stock = getStockOrThrowException(warehouseId,productID)
        stockRepository.delete(stock)
    }

    override fun getAllWarehousesHavingProduct(productID: String, pageIdx: Int, pageSize: Int): List<WarehouseDTO> {
        val page = getPageable(pageIdx, pageSize)

        return stockRepository.findAllByProduct(productID, page).map { it -> it.warehouse }.toList().map { it.toDTO() }
    }

    override fun getWarehouseHavingProducts(productList: List<PurchaseItemDTO>): List<ProductWarehouseDTO> {
        val list = mutableListOf<ProductWarehouseDTO>()

        productList.forEach{
            val stocks = stockRepository.findAllByProductAndQuantityIsGreaterThanEqual(it.productId, it.amount.toLong())
            if (stocks.isEmpty())
                throw  ItemsNotAvailable(it.productId, it.amount)
            list.add(ProductWarehouseDTO(stocks.first().warehouse.getId()!!.toString(),stocks.first().product))
        }

        return list
    }

    override fun updateAndRetrieveAmount(productList: List<PurchaseItemDTO>): List<OrderTransactionRequestDTO> {

        val list = mutableListOf<OrderTransactionRequestDTO>()

        productList.forEach{ it ->
            val stocks = stockRepository.findAllByProductAndQuantityIsGreaterThanEqual(it.productId, it.amount.toLong())
            if (stocks.isEmpty())
                throw  ItemsNotAvailable(it.productId, it.amount)
            val chosenStock = stocks.maxByOrNull { stock -> stock.quantity - stock.alarm }!!
            updateStockQuantity(chosenStock, chosenStock.quantity - it.amount)

            var price = it.price * BigDecimal(it.amount)
            
            val orderTransactionRequestDTO = list.find { o -> o.warehouseTo == chosenStock.warehouse.getId().toString()}
            orderTransactionRequestDTO?.also {  price += it.amount }
            list.remove(orderTransactionRequestDTO)
            list.add(OrderTransactionRequestDTO(chosenStock.warehouse.getId()!!.toString() ,price))
        }

        return list
    }

    override fun cancelRequestUpdate(productList: List<ItemsInWarehouseDTO<ItemDTO>>) {

        productList.forEach{ itemsInWarehouse ->
            itemsInWarehouse.purchaseItems.forEach{ item ->

                val warehouse = warehouseService.getWarehouseOrThrowException(itemsInWarehouse.warehouseId!!)
                val stock =  stockRepository.findByWarehouseAndProduct(warehouse,item.productId)
                if (  stock == null ) {
                    val newStock = Stock(
                        warehouse,
                        item.productId,
                        item.amount.toLong(),
                        item.amount.toLong(),
                    )

                    stockRepository.save(newStock)
                } else {
                   //  updateStockQuantity(stock, stock.quantity + item.amount) // questo invia la mail
                    stock.quantity +=  item.amount
                }
            }
        }
    }


    private fun getStockOrThrowException(warehouseId: String, productID: String) : Stock {
        val warehouse = warehouseService.getWarehouseOrThrowException(warehouseId)

        return stockRepository.findByWarehouseAndProduct(warehouse,productID) ?: throw StockNotFound(warehouseId,productID)
    }

}