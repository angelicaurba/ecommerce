package it.polito.wa2.ecommerce.walletservice.consumer

import it.polito.wa2.ecommerce.common.saga.service.ProcessingLogService
import it.polito.wa2.ecommerce.walletservice.client.order.request.OrderRequestDTO
import it.polito.wa2.ecommerce.walletservice.service.WalletService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import java.util.*

@Component
class OrderRequestConsumer{

    @Autowired
    lateinit var walletService: WalletService

    @Autowired
    lateinit var processingLogService: ProcessingLogService

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
        walletService.processOrderRequest(orderRequestDTO)
        processingLogService.process(uuid)

    }
}