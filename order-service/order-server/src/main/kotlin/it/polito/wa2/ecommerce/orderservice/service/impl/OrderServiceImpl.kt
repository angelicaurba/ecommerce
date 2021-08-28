package it.polito.wa2.ecommerce.orderservice.it.polito.wa2.ecommerce.orderservice.service.impl

import it.polito.wa2.ecommerce.orderservice.client.order.request.OrderRequestDTO
import it.polito.wa2.ecommerce.orderservice.client.item.PurchaseItemDTO
import it.polito.wa2.ecommerce.orderservice.client.UpdateOrderRequestDTO
import it.polito.wa2.ecommerce.orderservice.client.order.response.OrderDTO
import it.polito.wa2.ecommerce.orderservice.it.polito.wa2.ecommerce.orderservice.service.OrderService

class OrderServiceImpl: OrderService {
    override fun getAllOrders(pageIdx: Int, pageSize: Int): List<OrderDTO> {
        TODO("Not yet implemented")
        TODO("get all orders of buyerid in cookie, if it is an admin get all orders")
    }

    override fun getOrderById(orderId: String): OrderDTO {
        TODO("Not yet implemented")
        TODO("check that order with orderid belongs to current user or current user is admin")
    }

    override fun addOrder(orderRequest: OrderRequestDTO<PurchaseItemDTO>): OrderDTO {
        TODO("Not yet implemented")
    }

    override fun updateStatus(orderId: String, updateOrderRequest: UpdateOrderRequestDTO): OrderDTO {
        TODO("Not yet implemented")
        TODO("check that current user is admin")
        TODO("check that the state machine is respected")
        TODO("status in updateOrderRequest CANNOT be CANCELED")
    }

    override fun cancelOrder(orderId: String) {
        TODO("Not yet implemented")
        TODO("check that order with orderid belongs to current user or current user is admin")
        TODO("check that the state machine is respected")
        TODO("start refund")
    }
}