package it.polito.wa2.ecommerce.walletservice.config

import it.polito.wa2.ecommerce.common.kafka.BaseKafkaConsumerConfig
import it.polito.wa2.ecommerce.walletservice.client.order.request.WalletOrderRequestDTO
import org.springframework.context.annotation.Bean
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.stereotype.Component

@Component
class OrderRequestConsumerConfig: BaseKafkaConsumerConfig<WalletOrderRequestDTO>(WalletOrderRequestDTO::class.java){

    @Bean("orderRequestListenerFactory")
    fun orderRequestListenerFactory():
            ConcurrentKafkaListenerContainerFactory<String, WalletOrderRequestDTO>? {
        return super.concurrentKafkaListenerContainerFactory()
    }
}
