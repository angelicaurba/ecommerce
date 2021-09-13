package it.polito.wa2.ecommerce.walletservice.config

import it.polito.wa2.ecommerce.common.kafka.BaseKafkaConsumerConfig
import it.polito.wa2.ecommerce.walletservice.client.wallet.request.WalletCreationRequestDTO
import org.springframework.stereotype.Component

@Component
class WalletCreationRequestConsumerConfig: BaseKafkaConsumerConfig<WalletCreationRequestDTO>(WalletCreationRequestDTO::class.java)
