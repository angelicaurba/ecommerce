package it.polito.wa2.ecommerce.walletservice.consumer

import it.polito.wa2.ecommerce.common.constants.walletCreationTopic
import it.polito.wa2.ecommerce.walletservice.client.order.request.WalletOrderRequestDTO
import it.polito.wa2.ecommerce.walletservice.client.wallet.request.WalletCreationRequestDTO
import it.polito.wa2.ecommerce.walletservice.service.OrderProcessingService
import it.polito.wa2.ecommerce.walletservice.service.WalletService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class WalletCreationRequestConsumer{

    @Autowired
    lateinit var walletService: WalletService


    @KafkaListener( topics=[walletCreationTopic])
    fun listen(@Payload walletCreationRequestDTO: WalletCreationRequestDTO,
               @Header("id") id: String,
               @Header("eventType") eventType:String
    ){
        println("Processing message $id ($eventType) : $walletCreationRequestDTO")
        walletService.addWallet(walletCreationRequestDTO, verifySecurity = false)


    }
}