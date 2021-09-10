package it.polito.wa2.ecommerce.walletservice.service

import it.polito.wa2.ecommerce.orderservice.client.order.messages.OrderStatus
import it.polito.wa2.ecommerce.walletservice.client.order.request.WalletOrderRequestDTO

interface OrderProcessingService {

    fun processOrderRequest(orderRequestDTO: WalletOrderRequestDTO): OrderStatus?
    fun process(orderRequestDTO: WalletOrderRequestDTO, id: String)
}