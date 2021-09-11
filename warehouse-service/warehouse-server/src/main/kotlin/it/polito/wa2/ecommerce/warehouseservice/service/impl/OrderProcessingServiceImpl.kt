package it.polito.wa2.ecommerce.warehouseservice.service.impl

import it.polito.wa2.ecommerce.common.saga.service.MessageService
import it.polito.wa2.ecommerce.common.saga.service.ProcessingLogService
import it.polito.wa2.ecommerce.warehouseservice.client.order.request.WarehouseOrderRequestDTO
import it.polito.wa2.ecommerce.orderservice.client.order.messages.EventTypeOrderStatus
import it.polito.wa2.ecommerce.orderservice.client.order.messages.ResponseStatus
import it.polito.wa2.ecommerce.orderservice.client.order.messages.OrderStatus
import it.polito.wa2.ecommerce.warehouseservice.client.order.request.WarehouseOrderRequestCancelDTO
import it.polito.wa2.ecommerce.warehouseservice.client.order.request.WarehouseOrderRequestNewDTO
import it.polito.wa2.ecommerce.warehouseservice.service.OrderProcessingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(propagation = Propagation.NESTED)
class OrderProcessingServiceImpl: OrderProcessingService {



    @Autowired
    lateinit var processingLogService: ProcessingLogService

    @Autowired
    lateinit var messageService: MessageService

    @Autowired
    lateinit var self: OrderProcessingService

    override fun process(orderRequestDTO: WarehouseOrderRequestDTO, id: String){
        val uuid = UUID.fromString(id)
        if(processingLogService.isProcessed(uuid))
            return

        lateinit var status: OrderStatus
        try {
            status = self.processOrderRequest(orderRequestDTO)
        }
        catch (e:Exception){
            status = OrderStatus(
                orderRequestDTO.orderId,
                ResponseStatus.FAILED,
                e.message)
        }
        finally {
            processingLogService.process(uuid)
            status.also {
                messageService.publish(it,
                    if(it.responseStatus == ResponseStatus.COMPLETED)
                        EventTypeOrderStatus.OrderOk.toString()
                    else EventTypeOrderStatus.OrderPaymentFailed.toString(),
                    "order-status") //TODO define constant
            }
        }
    }

    override fun processOrderRequest(orderRequestDTO: WarehouseOrderRequestDTO): OrderStatus {
        TODO("Not yet implemented")
        /*
        val orderId = orderRequestDTO.orderId

        if (orderRequestDTO is WarehouseOrderRequestNewDTO) {

        } else if (orderRequestDTO is WarehouseOrderRequestCancelDTO) 

        return OrderStatus(
            orderId, when (orderRequestDTO.requestType) {
                OrderPaymentType.PAY -> Req.COMPLETED
                OrderPaymentType.REFUND -> OrderStatus.REFUNDED
            }, null
        )
        */
    }

}