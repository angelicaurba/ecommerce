package it.polito.wa2.ecommerce.walletservice.consumer

import it.polito.wa2.ecommerce.walletservice.client.order.request.WarehouseOrderRequestDTO
import it.polito.wa2.ecommerce.walletservice.service.OrderProcessingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class OrderRequestConsumer{

    @Autowired
    lateinit var orderProcessingService: OrderProcessingService


    //TODO set correct name of topic and listener
    @KafkaListener(id="test-reader-ecommerce", topics=["topic1"])
    fun listen(@Payload orderRequestDTO: WarehouseOrderRequestDTO,
               @Header("id") id: String,
               @Header("eventType") eventType:String
    ){
        println("Processing message $id ($eventType) : $orderRequestDTO")
        orderProcessingService.process(orderRequestDTO,id)


    }
}