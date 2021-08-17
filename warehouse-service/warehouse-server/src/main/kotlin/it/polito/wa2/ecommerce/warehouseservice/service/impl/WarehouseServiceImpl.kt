package it.polito.wa2.ecommerce.warehouseservice.service.impl

import it.polito.wa2.ecommerce.warehouseservice.client.WarehouseDTO
import it.polito.wa2.ecommerce.warehouseservice.service.WarehouseService
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class WarehouseServiceImpl : WarehouseService {
    override fun getAllWarehouses(): List<WarehouseDTO> {
        TODO("Not yet implemented")
    }

    override fun getWarehouseById(warehouseId: Long): WarehouseDTO {
        TODO("Not yet implemented")
    }

    override fun addWarehouse(warehouse: WarehouseDTO): WarehouseDTO {
        TODO("Not yet implemented")
    }

    override fun updateWarehouse(warehouseId: Long, warehouseDTO: Any): WarehouseDTO {
        TODO("Not yet implemented")
    }

    override fun deleteWarehouseById(warehouseId: Long): WarehouseDTO {
        TODO("Not yet implemented")
    }
}