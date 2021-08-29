package it.polito.wa2.ecommerce.walletservice.consumer

import it.polito.wa2.ecommerce.warehouseservice.client.order.request.WarehouseOrderRequestDTO
import it.polito.wa2.ecommerce.warehouseservice.service.OrderProcessingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class OrderRequestConsumer{

    @Autowired
    lateinit var orderProcessingService: OrderProcessingService


    @KafkaListener( topics=["payment-request"])
    fun listen(@Payload orderRequestDTO: WarehouseOrderRequestDTO,
               @Header("id") id: String,
               @Header("eventType") eventType:String
    ){
        println("Processing message $id ($eventType) : $orderRequestDTO")
        orderProcessingService.process(orderRequestDTO,id)


    }
}