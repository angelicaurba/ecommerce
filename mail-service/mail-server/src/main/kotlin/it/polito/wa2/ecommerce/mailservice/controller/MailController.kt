package it.polito.wa2.ecommerce.mailservice.controller

import it.polito.wa2.ecommerce.mailservice.client.MailDTO
import it.polito.wa2.ecommerce.mailservice.service.impl.MailServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

// TODO for debug purposes, it will be deleted when kafka/debezium is fully implemented
/*
@RestController
@RequestMapping("/mail")
@Validated
class MailController{
    @Autowired
    lateinit var mailService: MailServiceImpl

    @PostMapping("/")
    fun sendMail(
        @RequestBody mail: MailDTO
    ){
        mailService.sendMail(mail)
    }
}
*/