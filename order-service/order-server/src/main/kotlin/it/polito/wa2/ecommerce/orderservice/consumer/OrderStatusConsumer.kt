package it.polito.wa2.ecommerce.orderservice.consumer

import it.polito.wa2.ecommerce.common.constants.orderStatusTopic
import it.polito.wa2.ecommerce.orderservice.client.order.messages.EventTypeOrderStatus
import it.polito.wa2.ecommerce.orderservice.client.order.messages.OrderStatus
import it.polito.wa2.ecommerce.orderservice.service.OrderService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class OrderStatusConsumer {

    @Autowired
    lateinit var orderService: OrderService

    @KafkaListener(id="order-service", topics=[orderStatusTopic])
    fun listen(@Payload orderStatus: OrderStatus,
               @Header("id") id: String,
               @Header("eventType") eventType: EventTypeOrderStatus
    ){
        println("Processing message $id ($eventType) : $orderStatus")
        orderService.processOrderCompletion(orderStatus, id, eventType)


    }
}