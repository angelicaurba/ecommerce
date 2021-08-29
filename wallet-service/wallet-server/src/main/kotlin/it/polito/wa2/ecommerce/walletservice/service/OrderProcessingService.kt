package it.polito.wa2.ecommerce.walletservice.service

// TODO Check import
import it.polito.wa2.ecommerce.orderservice.client.order.messages.OrderStatus
import it.polito.wa2.ecommerce.walletservice.client.order.request.WarehouseOrderRequestDTO

interface OrderProcessingService {

    fun processOrderRequest(orderRequestDTO: WarehouseOrderRequestDTO): OrderStatus
    fun process(orderRequestDTO: WarehouseOrderRequestDTO, id: String)
}