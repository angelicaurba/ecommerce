package it.polito.wa2.ecommerce.walletservice.config

import it.polito.wa2.ecommerce.common.kafka.BaseKafkaConsumerConfig
import it.polito.wa2.ecommerce.walletservice.client.order.request.OrderRequestDTO
import org.springframework.stereotype.Component

@Component
class OrderRequestConsumerConfig: BaseKafkaConsumerConfig<OrderRequestDTO>(OrderRequestDTO::class.java)
