package it.polito.wa2.ecommerce.orderservice.service

import it.polito.wa2.ecommerce.orderservice.client.*
import it.polito.wa2.ecommerce.orderservice.client.order.request.OrderRequestDTO
import it.polito.wa2.ecommerce.orderservice.client.item.PurchaseItemDTO
import it.polito.wa2.ecommerce.orderservice.client.order.messages.EventTypeOrderStatus
import it.polito.wa2.ecommerce.orderservice.client.order.messages.OrderDetailsDTO
import it.polito.wa2.ecommerce.orderservice.client.order.messages.OrderStatusDTO
import it.polito.wa2.ecommerce.orderservice.client.order.response.OrderDTO

interface OrderService {
    fun getAllOrders(pageIdx: Int, pageSize: Int): List<OrderDTO>
    fun getOrderById(orderId: String): OrderDTO
    fun addOrder(orderRequest: OrderRequestDTO<PurchaseItemDTO>): OrderDTO
    fun updateStatus(orderId: String, updateOrderRequest: UpdateOrderRequestDTO): OrderDTO
    fun cancelOrder(orderId: String)
    fun processOrderCompletion(orderStatusDTO: OrderStatusDTO, id: String, eventType: EventTypeOrderStatus)
    fun process(orderDetailsDTO: OrderDetailsDTO, id: String)
}