package it.polito.wa2.ecommerce.orderservice.service.impl

import it.polito.wa2.ecommerce.common.Rolename
import it.polito.wa2.ecommerce.common.constants.mailTopic
import it.polito.wa2.ecommerce.common.constants.orderRequestTopic
import it.polito.wa2.ecommerce.common.constants.paymentTopic
import it.polito.wa2.ecommerce.common.parseID
import it.polito.wa2.ecommerce.common.saga.service.MessageService
import it.polito.wa2.ecommerce.common.saga.service.ProcessingLogService
import it.polito.wa2.ecommerce.mailservice.client.MailDTO
import it.polito.wa2.ecommerce.common.exceptions.ForbiddenException
import it.polito.wa2.ecommerce.common.exceptions.NotFoundException
import it.polito.wa2.ecommerce.common.getPageable
import it.polito.wa2.ecommerce.common.security.IdentityVerifier
import it.polito.wa2.ecommerce.common.security.JwtTokenDetails
import it.polito.wa2.ecommerce.orderservice.client.order.request.OrderRequestDTO
import it.polito.wa2.ecommerce.orderservice.client.item.PurchaseItemDTO
import it.polito.wa2.ecommerce.orderservice.client.UpdateOrderRequestDTO
import it.polito.wa2.ecommerce.orderservice.client.item.ItemDTO
import it.polito.wa2.ecommerce.orderservice.client.order.messages.EventTypeOrderStatus
import it.polito.wa2.ecommerce.orderservice.client.order.messages.OrderDetailsDTO
import it.polito.wa2.ecommerce.orderservice.client.order.messages.OrderStatusDTO
import it.polito.wa2.ecommerce.orderservice.client.order.messages.ResponseStatus
import it.polito.wa2.ecommerce.orderservice.client.order.response.OrderDTO
import it.polito.wa2.ecommerce.orderservice.client.order.response.Status
import it.polito.wa2.ecommerce.orderservice.domain.Order
import it.polito.wa2.ecommerce.orderservice.domain.toEntity
import it.polito.wa2.ecommerce.orderservice.exception.OrderNotFoundException
import it.polito.wa2.ecommerce.orderservice.repository.OrderRepository
import it.polito.wa2.ecommerce.orderservice.service.OrderService
import it.polito.wa2.ecommerce.orderservice.repository.PurchaseItemRepository
import it.polito.wa2.ecommerce.orderservice.utils.extractProductInWarehouse
import it.polito.wa2.ecommerce.walletservice.client.order.request.WalletOrderRefundRequestDTO
import it.polito.wa2.ecommerce.warehouseservice.client.order.request.WarehouseOrderRequestCancelDTO
import it.polito.wa2.ecommerce.warehouseservice.client.order.request.WarehouseOrderRequestNewDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(rollbackFor = [Exception::class])
class OrderServiceImpl: OrderService {

    @Autowired
    lateinit var processingLogService: ProcessingLogService

    @Autowired
    lateinit var orderRepository: OrderRepository

    @Autowired
    lateinit var purchaseItemRepository: PurchaseItemRepository

    @Autowired
    lateinit var messageService: MessageService

    @Autowired
    lateinit var identityVerifier: IdentityVerifier

    override fun getAllOrders(pageIdx: Int, pageSize: Int): List<OrderDTO> {
        val principal = SecurityContextHolder.getContext().authentication.principal as JwtTokenDetails
        val page = getPageable(pageIdx, pageSize)

        return if(principal.roles.contains(Rolename.ADMIN))
            orderRepository.findAll(page).toList().map { it.toDTO() }
        else
            orderRepository.findAllByBuyerId(principal.id, page).toList().map { it.toDTO() }
    }

    override fun getOrderById(orderId: String): OrderDTO {
        val order = getOrderOrThrowException(orderId)
        identityVerifier.verifyUserIdentityOrIsAdmin(order.buyerId.parseID())
        return order.toDTO()
    }

    override fun addOrder(orderRequest: OrderRequestDTO<PurchaseItemDTO>): OrderDTO {
        identityVerifier.verifyUserIdentityOrIsAdmin(orderRequest.buyerId.parseID())

        val newOrder = Order(
            orderRequest.buyerId,
            orderRequest.address,
            orderRequest.buyerWalletId,
            Status.PENDING
        )

        val addedOrder = orderRepository.save(newOrder)

        orderRequest.deliveryItems.map { it.toEntity() }.forEach {
            it.order = addedOrder
            addedOrder.deliveryItems.add(purchaseItemRepository.save(it))
        }

        val orderMessage = WarehouseOrderRequestNewDTO(
            addedOrder.getId().toString(),
            addedOrder.buyerId,
            addedOrder.buyerWalletId,
            addedOrder.deliveryItems.map { it.toDTO() }
        )

        messageService.publish(orderMessage, "NewOrder", orderRequestTopic)

        return addedOrder.toDTO()

    }

    override fun updateStatus(orderId: String, updateOrderRequest: UpdateOrderRequestDTO): OrderDTO {

        identityVerifier.verifyIsAdmin()

        if(updateOrderRequest.status == Status.CANCELED)
            throw ForbiddenException()

        val order = getOrderOrThrowException(orderId)
        order.updateStatus(updateOrderRequest.status)

        val mail = MailDTO(
            order.buyerId, null,
            "Your order's status has been correctly updated: $orderId",
            "The order's status has been correctly updated to ${order.status}"
        )
        messageService.publish(mail, "OrderStatusUpdated", mailTopic)

        return orderRepository.save(order).toDTO()
    }

    override fun cancelOrder(orderId: String) {

        val order = getOrderOrThrowException(orderId)
        identityVerifier.verifyUserIdentityOrIsAdmin(order.buyerId.parseID())

        order.updateStatus(Status.CANCELED)
        orderRepository.save(order)

        val mail = MailDTO(
            order.buyerId, null,
            "Your order has been correctly canceled: $orderId",
            "The order has been correctly canceled"
        )
        messageService.publish(mail, "OrderCanceled", mailTopic)

        val cancelMessage = WarehouseOrderRequestCancelDTO(
            orderId,
            order.deliveryItems.extractProductInWarehouse { ItemDTO(it.productId, it.amount) }
        )
        messageService.publish(cancelMessage, "OrderCancel", orderRequestTopic)

        val orderRefundRequest = WalletOrderRefundRequestDTO(order.buyerWalletId, order.buyerId, orderId)
        messageService.publish(orderRefundRequest, "OrderCancel", paymentTopic)
    }

    override fun processOrderCompletion(orderStatusDTO: OrderStatusDTO, id: String, eventType: EventTypeOrderStatus) {
        val eventID = UUID.fromString(id)
        if(processingLogService.isProcessed(eventID))
            return

        val orderId = orderStatusDTO.orderID.parseID()
        val order = orderRepository.findByIdOrNull(orderId) ?: throw OrderNotFoundException(orderId)
        when(orderStatusDTO.responseStatus) {
            ResponseStatus.COMPLETED -> {

                // - set status to ISSUED
                order.updateStatus(Status.ISSUED)

                // - send email
                val mail: MailDTO = MailDTO(
                    order.buyerId, null,
                    "Your order has been issued: $orderId",
                    "The order has been correctly issued"
                )
                messageService.publish(mail, "OrderIssued", mailTopic)
                orderRepository.save(order)

            }
            ResponseStatus.FAILED -> {

                // - set status to FAILED
                order.updateStatus(Status.FAILED)
                if (eventType == EventTypeOrderStatus.OrderPaymentFailed) {
                    // - if payment error rollback warehouse
                    val request = WarehouseOrderRequestCancelDTO(orderId.toString(),
                        order.deliveryItems.extractProductInWarehouse { ItemDTO(it.productId, it.amount) })

                    messageService.publish(request,  "OrderCancel", orderRequestTopic) 
                }

                // - send email
                val mail: MailDTO = MailDTO(
                    order.buyerId, null,
                    "Your order has failed issued: $orderId",
                    "The order was not issued.\nError message: ${orderStatusDTO.errorMessage}"
                )
                messageService.publish(mail, "OrderIssued", mailTopic)
                orderRepository.save(order)
            }
        }
        processingLogService.process(eventID)

    }

    override fun process(orderDetailsDTO: OrderDetailsDTO, id: String) {
        val eventID = UUID.fromString(id)
        if(processingLogService.isProcessed(eventID))
            return

        val order = getOrderOrThrowException(orderDetailsDTO.orderId)
        orderDetailsDTO.productWarehouseList.forEach {
            productWarehouseDTO ->
             purchaseItemRepository.setWarehouseByOrderAndProduct(order, productWarehouseDTO.productId, productWarehouseDTO.warehouseId )

        }
        processingLogService.process(eventID)
    }

    private fun getOrderOrThrowException(orderId: String): Order {
        return orderRepository.findByIdOrNull(orderId.parseID()) ?: throw NotFoundException("Cannot find order with id: $orderId")
    }


}