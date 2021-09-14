package it.polito.wa2.ecommerce.walletservice.consumer

import it.polito.wa2.ecommerce.common.constants.paymentTopic
import it.polito.wa2.ecommerce.walletservice.client.order.request.WalletOrderRequestDTO
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


    @KafkaListener( topics=[paymentTopic],
        containerFactory="orderRequestListenerFactory")
    fun listen(@Payload orderRequestDTO: WalletOrderRequestDTO,
               @Header("id") id: String,
               @Header("eventType") eventType:String
    ){
        println("Processing message $id ($eventType) : $orderRequestDTO")
        orderProcessingService.process(orderRequestDTO,id)


    }
}