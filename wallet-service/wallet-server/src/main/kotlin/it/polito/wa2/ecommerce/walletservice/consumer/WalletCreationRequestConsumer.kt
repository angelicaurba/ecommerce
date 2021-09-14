package it.polito.wa2.ecommerce.walletservice.consumer

import it.polito.wa2.ecommerce.common.constants.walletCreationTopic
import it.polito.wa2.ecommerce.walletservice.client.wallet.request.WalletCreationRequestDTO
import it.polito.wa2.ecommerce.walletservice.service.WalletService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import javax.validation.Valid

@Component
class WalletCreationRequestConsumer{

    @Autowired
    lateinit var walletService: WalletService


    @KafkaListener( topics=[walletCreationTopic],
        containerFactory="walletCreationListenerFactory")
    fun listen(@Payload @Valid walletCreationRequestDTO: WalletCreationRequestDTO,
               @Header("id") id: String,
               @Header("eventType") eventType:String
    ){
        throw RuntimeException("test")
        println("Processing message $id ($eventType) : $walletCreationRequestDTO")
        walletService.addWallet(walletCreationRequestDTO, verifySecurity = false)


    }
}