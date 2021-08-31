package it.polito.wa2.ecommerce.orderservice.service.impl

import it.polito.wa2.ecommerce.common.parseID
import it.polito.wa2.ecommerce.common.saga.service.ProcessingLogService
import it.polito.wa2.ecommerce.common.Rolename
import it.polito.wa2.ecommerce.common.getPageable
import it.polito.wa2.ecommerce.common.security.UserDetailsDTO
import org.springframework.security.core.context.SecurityContextHolder
import it.polito.wa2.ecommerce.orderservice.client.order.request.OrderRequestDTO
import it.polito.wa2.ecommerce.orderservice.client.item.PurchaseItemDTO
import it.polito.wa2.ecommerce.orderservice.client.UpdateOrderRequestDTO
import it.polito.wa2.ecommerce.orderservice.client.order.messages.EventTypeOrderStatus
import it.polito.wa2.ecommerce.orderservice.client.order.messages.OrderDetailsDTO
import it.polito.wa2.ecommerce.orderservice.client.order.messages.OrderStatus
import it.polito.wa2.ecommerce.orderservice.client.order.messages.Status
import it.polito.wa2.ecommerce.orderservice.client.order.response.OrderDTO
import it.polito.wa2.ecommerce.orderservice.repository.OrderRepository
import it.polito.wa2.ecommerce.orderservice.service.OrderService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class OrderServiceImpl: OrderService {

    @Autowired
    lateinit var processingLogService: ProcessingLogService

    @Autowired
    lateinit var orderRepository: OrderRepository

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

    override fun processOrderCompletion(orderStatus: OrderStatus, id: String, eventType: EventTypeOrderStatus) {
        val eventID = UUID.fromString(id)
        if(processingLogService.isProcessed(eventID))
            return
        when(orderStatus.status){
            Status.COMPLETED->{
                // TODO:
                // - set status to ISSUED
                // - send email
            }
            Status.FAILED-> {
                // TODO:
                // - cancel order
                // - if payment error rollback warehouse
                // - set status to FAILED
                // - send email
            }
            Status.REFUNDED ->{
                // TODO:
                // - set to CANCELLED
                // - send email (?)
            }
        }

        processingLogService.process(eventID)

    }

    override fun process(orderDetailsDTO: OrderDetailsDTO, id: String) {
        val eventID = UUID.fromString(id)
        if(processingLogService.isProcessed(eventID))
            return

        // TODO:
        // - update products for order

        orderDetailsDTO.productWarehouseList.forEach {
            productWarehouseDTO ->
        }
        processingLogService.process(eventID)
    }


}