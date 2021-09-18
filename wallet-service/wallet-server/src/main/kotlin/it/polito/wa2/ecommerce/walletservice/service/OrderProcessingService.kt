package it.polito.wa2.ecommerce.walletservice.service

import it.polito.wa2.ecommerce.orderservice.client.order.messages.OrderStatusDTO
import it.polito.wa2.ecommerce.walletservice.client.order.request.WalletOrderRequestDTO

interface OrderProcessingService {

    fun processOrderRequest(orderRequestDTO: WalletOrderRequestDTO): OrderStatusDTO?
    fun process(orderRequestDTO: WalletOrderRequestDTO, id: String)
}