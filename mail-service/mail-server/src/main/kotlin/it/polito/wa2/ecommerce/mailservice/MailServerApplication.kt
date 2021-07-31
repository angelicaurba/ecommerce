package it.polito.wa2.ecommerce.mailservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MailServerApplication

fun main(args: Array<String>){
    runApplication<MailServerApplication>(*args)
}