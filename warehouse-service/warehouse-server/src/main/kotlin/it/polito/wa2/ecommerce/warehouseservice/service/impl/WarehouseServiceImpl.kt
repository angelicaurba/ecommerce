package it.polito.wa2.ecommerce.warehouseservice.service.impl

import it.polito.wa2.ecommerce.common.getPageable
import it.polito.wa2.ecommerce.common.parseID
import it.polito.wa2.ecommerce.warehouseservice.client.WarehouseDTO
import it.polito.wa2.ecommerce.warehouseservice.client.WarehouseRequestDTO
import it.polito.wa2.ecommerce.warehouseservice.domain.Warehouse
import it.polito.wa2.ecommerce.warehouseservice.exception.WarehouseNotFound
import it.polito.wa2.ecommerce.warehouseservice.repository.WarehouseRepository
import it.polito.wa2.ecommerce.warehouseservice.service.WarehouseService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class WarehouseServiceImpl : WarehouseService {

    @Autowired lateinit var warehouseRepository: WarehouseRepository

    fun getWarehouseOrThrowException(warehouseId: String) : Warehouse {
        return warehouseRepository.findByIdOrNull(warehouseId.parseID()) ?: throw WarehouseNotFound(warehouseId)
    }

    override fun deleteWarehouseById(warehouseId: String) {
        val warehouse = getWarehouseOrThrowException(warehouseId)
        warehouseRepository.delete(warehouse)
    }

    override fun updateWarehouseFields(warehouseId: String, warehouseRequest: WarehouseRequestDTO): WarehouseDTO {
        val warehouse = getWarehouseOrThrowException(warehouseId)

        warehouseRequest.name?.also { warehouse.name = it }
        warehouseRequest.address?.also { warehouse.address = it }
        warehouseRequest.adminID?.also { warehouse.adminID = it }

        return warehouseRepository.save(warehouse).toDTO()
    }

    override fun updateOrCreateWarehouse(warehouseId: String, warehouseRequest: WarehouseRequestDTO): WarehouseDTO {

        if ( warehouseRepository.findById(warehouseId.parseID()).isPresent ){
            val warehouse : Warehouse = warehouseRepository.findById(warehouseId.parseID()).get()
            warehouse.name = warehouseRequest.name!!
            warehouse.address = warehouseRequest.address!!
            warehouse.adminID = warehouseRequest.adminID!!

             return warehouseRepository.save(warehouse).toDTO()
        } else
            return addWarehouse(warehouseRequest, warehouseId)
    }

    override fun addWarehouse(warehouseRequest: WarehouseRequestDTO, warehouseId: String?): WarehouseDTO {
        // TODO handle warehouseId : save + query custom to update id
        val newWarehouse = Warehouse(
            warehouseRequest.name!!,
            warehouseRequest.address!!,
            warehouseRequest.adminID!!
        )
        /*
        if (warehouseId != null) {
            val oldID = warehouseRepository.save(newWarehouse).getId()
            warehouseRepository.updateID(warehouseId,oldID)
            return getWarehouseOrThrowException(warehouseId).toDTO()
        } else */
            return warehouseRepository.save(newWarehouse).toDTO()
    }

    override fun getWarehouseById(warehouseId: String): WarehouseDTO {
        val warehouse = getWarehouseOrThrowException(warehouseId)

        return warehouse.toDTO()
    }

    override fun getAllWarehouses(pageIdx: Int, pageSize: Int): List<WarehouseDTO> {
        val page = getPageable(pageIdx, pageSize)

        return warehouseRepository.findAll(page).toList().map { it.toDTO() }
    }

}