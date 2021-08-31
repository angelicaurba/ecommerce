package it.polito.wa2.ecommerce.orderservice.config

import it.polito.wa2.ecommerce.common.kafka.BaseKafkaConsumerConfig
import it.polito.wa2.ecommerce.orderservice.client.order.messages.OrderStatus
import org.springframework.stereotype.Component

@Component
class OrderStatusConsumerConfig : BaseKafkaConsumerConfig<OrderStatus>(OrderStatus::class.java)
