package it.polito.wa2.ecommerce.mailservice.consumer

import it.polito.wa2.ecommerce.common.constants.mailTopic
import it.polito.wa2.ecommerce.mailservice.client.MailDTO
import it.polito.wa2.ecommerce.mailservice.service.impl.MailServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import javax.validation.Valid

@Component
class MailListener {
    @Autowired
    lateinit var mailService: MailServiceImpl

    @KafkaListener(
        topics = [mailTopic],
        containerFactory = "concurrentKafkaListenerContainerFactory"
    )
    fun mailListener(@Payload @Valid mailDTO: MailDTO,
                     @Header("id") id: String,
                     @Header("eventType") eventType:String) {
        println("Processing message $id ($eventType) : $mailDTO")
        mailService.sendMail(mailDTO, id)
    }
}