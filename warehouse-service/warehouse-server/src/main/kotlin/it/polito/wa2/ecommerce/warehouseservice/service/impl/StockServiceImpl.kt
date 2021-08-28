package it.polito.wa2.ecommerce.warehouseservice.service.impl

import it.polito.wa2.ecommerce.common.getPageable
import it.polito.wa2.ecommerce.warehouseservice.client.StockDTO
import it.polito.wa2.ecommerce.warehouseservice.client.StockRequestDTO
import it.polito.wa2.ecommerce.warehouseservice.domain.Stock
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

    override fun updateStockFields(warehouseId: String, productID: String, stockRequest: StockRequestDTO): StockDTO {
        // TODO check if quantity is less than alarm here, product should exist
        var stock = getStockOrThrowException(warehouseId,productID)

        stockRequest.warehouseID?.also {
            val warehouse = warehouseService.getWarehouseOrThrowException(stockRequest.warehouseID!!)
            stock.warehouse = warehouse }
        stockRequest.productID?.also { stock.product = it }
        stockRequest.quantity?.also { stock.quantity = it }
        stockRequest.alarm?.also { stock.alarm = it }
        return stockRepository.save(stock).toDTO()
    }

    override fun updateOrCreateStock(
        warehouseId: String,
        productId: String,
        stockRequestDTO: StockRequestDTO
    ): StockDTO {
        // TODO check if quantity is less than alarm here, product should exist
        var warehouse = warehouseService.getWarehouseOrThrowException(warehouseId)
        val stock = stockRepository.findByWarehouseAndProduct(warehouse,productId)

        if ( stock != null ){

            if (warehouseId != stockRequestDTO.warehouseID){
                warehouse = warehouseService.getWarehouseOrThrowException(stockRequestDTO.warehouseID!!)
                stock.warehouse = warehouse
            }

            stock.product = stockRequestDTO.productID!!
            stock.quantity = stockRequestDTO.quantity!!
            stock.alarm = stockRequestDTO.alarm!!

            return stockRepository.save(stock).toDTO()
        } else
            return addStock(warehouseId, stockRequestDTO) // TODO which warehouseId should we pass?
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


    private fun getStockOrThrowException(warehouseId: String, productID: String) : Stock {
        val warehouse = warehouseService.getWarehouseOrThrowException(warehouseId)

        return stockRepository.findByWarehouseAndProduct(warehouse,productID) ?: throw StockNotFound(warehouseId,productID)
    }

}