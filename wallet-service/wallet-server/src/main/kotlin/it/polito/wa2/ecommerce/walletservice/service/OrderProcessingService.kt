package it.polito.wa2.ecommerce.walletservice.service

import it.polito.wa2.ecommerce.walletservice.client.order.OrderStatus
import it.polito.wa2.ecommerce.walletservice.client.order.request.OrderRequestDTO

interface OrderProcessingService {

    fun processOrderRequest(orderRequestDTO: OrderRequestDTO): OrderStatus
}