package it.polito.wa2.ecommerce.warehouseservice.service

interface OrderProcessingService {
    fun process(orderRequestDTO: Any, id: String)
}