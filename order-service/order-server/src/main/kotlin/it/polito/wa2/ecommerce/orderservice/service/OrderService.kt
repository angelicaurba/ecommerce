package it.polito.wa2.ecommerce.orderservice.it.polito.wa2.ecommerce.orderservice.service

import it.polito.wa2.ecommerce.orderservice.client.*
import it.polito.wa2.ecommerce.orderservice.client.order.request.OrderRequestDTO
import it.polito.wa2.ecommerce.orderservice.client.item.PurchaseItemDTO
import it.polito.wa2.ecommerce.orderservice.client.order.response.OrderDTO

interface OrderService {
    fun getAllOrders(pageIdx: Int, pageSize: Int): List<OrderDTO>
    fun getOrderById(orderId: String): OrderDTO
    fun addOrder(orderRequest: OrderRequestDTO<PurchaseItemDTO>): OrderDTO
    fun updateStatus(orderId: String, updateOrderRequest: UpdateOrderRequestDTO): OrderDTO
    fun cancelOrder(orderId: String)
}