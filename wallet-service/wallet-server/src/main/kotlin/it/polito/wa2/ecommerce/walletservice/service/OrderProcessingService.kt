package it.polito.wa2.ecommerce.walletservice.service

import it.polito.wa2.ecommerce.walletservice.client.order.OrderStatus
import it.polito.wa2.ecommerce.walletservice.client.order.request.WarehouseOrderRequestDTO

interface OrderProcessingService {

    fun processOrderRequest(orderRequestDTO: WarehouseOrderRequestDTO): OrderStatus
    fun process(orderRequestDTO: WarehouseOrderRequestDTO, id: String)
}