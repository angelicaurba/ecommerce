package it.polito.wa2.ecommerce.orderservice.config

import it.polito.wa2.ecommerce.common.kafka.BaseKafkaConsumerConfig
import it.polito.wa2.ecommerce.orderservice.client.order.messages.OrderStatusDTO
import org.springframework.context.annotation.Bean
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.stereotype.Component

@Component
class OrderStatusConsumerConfig : BaseKafkaConsumerConfig<OrderStatusDTO>(OrderStatusDTO::class.java){
    
    @Bean
    fun orderStatusListenerFactory(): ConcurrentKafkaListenerContainerFactory<String, OrderStatusDTO>? {
        return super.concurrentKafkaListenerContainerFactory()
    }
}
