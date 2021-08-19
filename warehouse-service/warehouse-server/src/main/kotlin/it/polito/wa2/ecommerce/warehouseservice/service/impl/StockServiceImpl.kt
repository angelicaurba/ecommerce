package it.polito.wa2.ecommerce.warehouseservice.service.impl

import it.polito.wa2.ecommerce.warehouseservice.client.StockDTO
import it.polito.wa2.ecommerce.warehouseservice.service.StockService
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class StockServiceImpl: StockService {

    override fun getStocksByWarehouseID(warehouseId: Long): StockDTO {
        TODO("Not yet implemented")
    }

    override fun getStockByWarehouseIDandProductID(warehouseId: Long, productID: Long): StockDTO {
        TODO("Not yet implemented")
    }

    override fun addStock(warehouseId: Long, stock: StockDTO): StockDTO {
        TODO("Not yet implemented")
    }
}