/*
package it.polito.wa2.ecommerce.mailservice.consumer

import it.polito.wa2.ecommerce.mailservice.client.MailDTO
import it.polito.wa2.ecommerce.mailservice.service.impl.MailServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class MailListener {
    @Autowired
    lateinit var mailService: MailServiceImpl

    @KafkaListener(
        topics = ["mail"],
        containerFactory = "kafkaListenerContainerFactory")
    fun mailListener(mail: MailDTO) {
        mailService.sendMail(mail)
    }
}
*/