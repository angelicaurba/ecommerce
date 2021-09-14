package it.polito.wa2.ecommerce.walletservice.config

import it.polito.wa2.ecommerce.common.kafka.BaseKafkaConsumerConfig
import it.polito.wa2.ecommerce.walletservice.client.order.request.WalletOrderRequestDTO
import it.polito.wa2.ecommerce.walletservice.client.wallet.request.WalletCreationRequestDTO
import org.springframework.context.annotation.Bean
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.stereotype.Component

@Component
class WalletCreationRequestConsumerConfig: BaseKafkaConsumerConfig<WalletCreationRequestDTO>(WalletCreationRequestDTO::class.java){

    @Bean("walletCreationListenerFactory")
    fun walletCreationListenerFactory():
            ConcurrentKafkaListenerContainerFactory<String, WalletCreationRequestDTO>? {
        return super.concurrentKafkaListenerContainerFactory()
    }
}
