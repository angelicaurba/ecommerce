package it.polito.wa2.ecommerce.orderservice.consumer

import it.polito.wa2.ecommerce.orderservice.client.order.messages.OrderDetailsDTO
import it.polito.wa2.ecommerce.orderservice.service.OrderService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class OrderDetailsConsumer{

    @Autowired
    lateinit var orderService: OrderService

    @KafkaListener( topics=["order-details"])
    fun listen(@Payload orderDetailsDTO: OrderDetailsDTO,
               @Header("id") id: String,
               @Header("eventType") eventType:String
    ){
        println("Processing message $id ($eventType) : $orderDetailsDTO")
        orderService.process(orderDetailsDTO,id)
    }
}