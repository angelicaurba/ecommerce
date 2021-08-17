package it.polito.wa2.ecommerce.warehouseservice.service

import it.polito.wa2.ecommerce.warehouseservice.client.WarehouseDTO

interface WarehouseService {
    fun getAllWarehouses(): List<WarehouseDTO>
    fun getWarehouseById(warehouseId: Long): WarehouseDTO
    fun addWarehouse(warehouse: WarehouseDTO): WarehouseDTO
    fun updateWarehouse(warehouseId: Long, warehouseDTO: Any): WarehouseDTO
    fun deleteWarehouseById(warehouseId: Long): WarehouseDTO
}