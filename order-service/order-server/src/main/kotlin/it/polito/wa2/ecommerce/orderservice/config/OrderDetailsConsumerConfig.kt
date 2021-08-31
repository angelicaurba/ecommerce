package it.polito.wa2.ecommerce.orderservice.config

import it.polito.wa2.ecommerce.common.kafka.BaseKafkaConsumerConfig
import it.polito.wa2.ecommerce.orderservice.client.order.messages.OrderDetailsDTO
import org.springframework.stereotype.Component

@Component
class OrderDetailsConsumerConfig : BaseKafkaConsumerConfig<OrderDetailsDTO>(OrderDetailsDTO::class.java)