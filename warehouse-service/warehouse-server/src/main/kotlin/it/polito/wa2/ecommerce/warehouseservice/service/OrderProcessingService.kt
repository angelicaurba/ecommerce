package it.polito.wa2.ecommerce.warehouseservice.service

import it.polito.wa2.ecommerce.warehouseservice.client.order.request.WarehouseOrderRequestDTO

interface OrderProcessingService {
    fun process(orderRequestDTO: WarehouseOrderRequestDTO, id: String)
}