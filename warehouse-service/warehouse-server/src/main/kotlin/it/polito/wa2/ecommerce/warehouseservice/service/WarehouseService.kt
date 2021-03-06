package it.polito.wa2.ecommerce.warehouseservice.service

import it.polito.wa2.ecommerce.warehouseservice.client.WarehouseDTO
import it.polito.wa2.ecommerce.warehouseservice.client.WarehouseRequestDTO
import it.polito.wa2.ecommerce.warehouseservice.domain.Warehouse

interface WarehouseService {
    fun deleteWarehouseById(warehouseId: String)
    fun updateWarehouseFields(warehouseId: String, warehouseRequest: WarehouseRequestDTO): WarehouseDTO
    fun updateOrCreateWarehouse(warehouseId: String, warehouseRequest: WarehouseRequestDTO): WarehouseDTO
    fun addWarehouse(warehouseRequest: WarehouseRequestDTO, warehouseId: String? = null): WarehouseDTO
    fun getWarehouseById(warehouseId: String): WarehouseDTO
    fun getAllWarehouses(pageIdx: Int, pageSize: Int): List<WarehouseDTO>
    fun isAuthorized(warehouseAdminID: String)
    fun getWarehouseOrThrowException(warehouseId: String) : Warehouse
}