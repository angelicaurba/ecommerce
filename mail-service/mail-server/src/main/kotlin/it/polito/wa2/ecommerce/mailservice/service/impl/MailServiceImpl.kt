package it.polito.wa2.ecommerce.mailservice.service.impl

import it.polito.wa2.ecommerce.common.connection.Request
import it.polito.wa2.ecommerce.common.saga.service.ProcessingLogService
import it.polito.wa2.ecommerce.mailservice.client.MailDTO
import it.polito.wa2.ecommerce.mailservice.service.MailService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(propagation = Propagation.NESTED)
class MailServiceImpl: MailService{

    @Autowired
    lateinit var mailSender: JavaMailSender

    @Autowired
    lateinit var processingLogService: ProcessingLogService

    @Value("\${spring.mail.username}")
    lateinit var fromMail : String

    @Autowired
    lateinit var request: Request

    override fun sendMail(mail: MailDTO, id: String){
        val uuid = UUID.fromString(id)

        if(processingLogService.isProcessed(uuid)){
            return
        }

        if (mail.userEmail == null){
            val uri = "http://user-service/users/${mail.userId}/email"
            mail.userEmail = request.doGet(uri, String::class.java)
        }

        val mailMessage = SimpleMailMessage()
        mailMessage.setSubject(mail.subject)
        mailMessage.setText(mail.mailBody)
        mailMessage.setTo(mail.userEmail)
        mailMessage.setFrom(fromMail)

        mailSender.send(mailMessage)

        processingLogService.process(uuid)
    }
}