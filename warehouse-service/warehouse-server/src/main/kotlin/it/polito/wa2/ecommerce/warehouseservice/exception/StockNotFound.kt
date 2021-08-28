package it.polito.wa2.ecommerce.warehouseservice.exception

import it.polito.wa2.ecommerce.common.exceptions.NotFoundException

class StockNotFound (warehouseId: String, productId: String)
    : NotFoundException("Cannot find stock with warehouseId $warehouseId and productId $productId")