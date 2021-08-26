package it.polito.wa2.ecommerce.walletservice.consumer

import it.polito.wa2.ecommerce.common.saga.service.MessageService
import it.polito.wa2.ecommerce.common.saga.service.ProcessingLogService
import it.polito.wa2.ecommerce.walletservice.client.order.OrderStatus
import it.polito.wa2.ecommerce.walletservice.client.order.Status
import it.polito.wa2.ecommerce.walletservice.client.order.request.OrderRequestDTO
import it.polito.wa2.ecommerce.walletservice.service.OrderProcessingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Component
@Transactional //TODO ok to put it transactional? Or put logic in service logic?
class OrderRequestConsumer{

    @Autowired
    lateinit var orderProcessingService: OrderProcessingService

    @Autowired
    lateinit var processingLogService: ProcessingLogService

    @Autowired
    lateinit var messageService: MessageService

    //TODO set correct name of topic and listener
    @KafkaListener(id="test-reader-ecommerce", topics=["topic1"])
    fun listen(@Payload orderRequestDTO: OrderRequestDTO,
               @Header("id") id: String,
               @Header("eventType") eventType:String
    ){
        val uuid = UUID.fromString(id)
        if(processingLogService.isProcessed(uuid))
            return
        println("Processing message $uuid ($eventType) : $orderRequestDTO")
        lateinit var status:OrderStatus
        try {
            status = orderProcessingService.processOrderRequest(orderRequestDTO)
        }
        catch (e:Exception){
            status = OrderStatus(
            orderRequestDTO.orderId,
            Status.FAILED,
            e.message)
        }
        finally {
            processingLogService.process(uuid)
            messageService.publish(status, "ORDER-${status.status}", "order") //TODO define name
        }


    }
}