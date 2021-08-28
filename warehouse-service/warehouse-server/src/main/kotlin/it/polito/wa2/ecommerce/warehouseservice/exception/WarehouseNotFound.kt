package it.polito.wa2.ecommerce.warehouseservice.exception

import it.polito.wa2.ecommerce.common.exceptions.NotFoundException

class WarehouseNotFound (warehouseId: String): NotFoundException("Cannot find warehouse with ID $warehouseId")