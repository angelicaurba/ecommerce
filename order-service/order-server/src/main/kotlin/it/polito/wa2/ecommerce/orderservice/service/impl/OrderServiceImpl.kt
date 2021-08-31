package it.polito.wa2.ecommerce.orderservice.service.impl

import it.polito.wa2.ecommerce.common.parseID
import it.polito.wa2.ecommerce.common.saga.service.MessageService
import it.polito.wa2.ecommerce.common.saga.service.ProcessingLogService
import it.polito.wa2.ecommerce.mailservice.client.MailDTO
import it.polito.wa2.ecommerce.orderservice.client.order.request.OrderRequestDTO
import it.polito.wa2.ecommerce.orderservice.client.item.PurchaseItemDTO
import it.polito.wa2.ecommerce.orderservice.client.UpdateOrderRequestDTO
import it.polito.wa2.ecommerce.orderservice.client.item.ItemDTO
import it.polito.wa2.ecommerce.orderservice.client.order.messages.EventTypeOrderStatus
import it.polito.wa2.ecommerce.orderservice.client.order.messages.OrderDetailsDTO
import it.polito.wa2.ecommerce.orderservice.client.order.messages.OrderStatus
import it.polito.wa2.ecommerce.orderservice.client.order.messages.ResponseStatus
import it.polito.wa2.ecommerce.orderservice.client.order.response.OrderDTO
import it.polito.wa2.ecommerce.orderservice.client.order.response.Status
import it.polito.wa2.ecommerce.orderservice.repository.OrderRepository
import it.polito.wa2.ecommerce.orderservice.service.OrderService
import it.polito.wa2.ecommerce.orderservice.repository.PurchaseItemRepository
import it.polito.wa2.ecommerce.orderservice.utils.extractProductInWarehouse
import it.polito.wa2.ecommerce.warehouseservice.client.order.request.WarehouseOrderRequestCancelDTO
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

    @Autowired
    lateinit var purchaseItemRepository: PurchaseItemRepository

    @Autowired
    lateinit var messageService: MessageService

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

        val orderId = orderStatus.orderID.parseID()
        val order = orderRepository.findByIdOrNull(orderId) ?: throw RuntimeException("Cannot find oder n. $orderId") //TODO exceptions?
        when(orderStatus.responseStatus){
            ResponseStatus.COMPLETED->{

                // - set status to ISSUED
                order.updateStatus(Status.ISSUED) // TODO add function to verify status correctness

                // - send email
                val mail:MailDTO = MailDTO(order.buyerId, null,
                    "Your order has been issued: $orderId",
                            "The order has been correctly issued")
//                messageService.publish(mail, "OrderIssued", "mail") // TODO uncomment //TODO add constants for topics
                orderRepository.save(order)

            }
            ResponseStatus.FAILED-> {

                // - set status to FAILED
                order.updateStatus( Status.FAILED)
                if(eventType == EventTypeOrderStatus.OrderPaymentFailed){
                    // - if payment error rollback warehouse
                    val request = WarehouseOrderRequestCancelDTO(orderId.toString(),
                        order.deliveryItems.extractProductInWarehouse { ItemDTO(it.productId, it.amount) })

                    messageService.publish(request, "order-request", "OrderCancel") //TODO add constants for topics
                }

                // - send email
                val mail:MailDTO = MailDTO(order.buyerId, null,
                    "Your order has failed issued: $orderId",
                    "The order was not issued.\nError message: ${orderStatus.errorMessage}")
//                messageService.publish(mail, "OrderIssued", "mail") // TODO uncomment //TODO add constants for topics
                orderRepository.save(order)
            }
        }

        processingLogService.process(eventID)

    }

    override fun process(orderDetailsDTO: OrderDetailsDTO, id: String) {
        val eventID = UUID.fromString(id)
        if(processingLogService.isProcessed(eventID))
            return

        val orderId : Long = orderDetailsDTO.orderId.parseID()
        orderDetailsDTO.productWarehouseList.forEach {
            productWarehouseDTO ->
             purchaseItemRepository.updateWarehouseByOrderAndProduct(orderId, productWarehouseDTO.productId, productWarehouseDTO.warehouseId )

        }
        processingLogService.process(eventID)
    }


}