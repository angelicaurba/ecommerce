package it.polito.wa2.ecommerce.mailservice.service

import it.polito.wa2.ecommerce.mailservice.client.MailDTO

interface MailService {
    fun sendMail(mail: MailDTO, id: String)
}