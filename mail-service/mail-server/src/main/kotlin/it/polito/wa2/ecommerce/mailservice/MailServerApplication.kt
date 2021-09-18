package it.polito.wa2.ecommerce.mailservice

import it.polito.wa2.ecommerce.common.CommonApplication
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.context.annotation.Bean
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import java.util.*

@SpringBootApplication(scanBasePackages = [
    "it.polito.wa2.ecommerce.mailservice",
    "it.polito.wa2.ecommerce.common.connection",
    "it.polito.wa2.ecommerce.common.saga"],
    scanBasePackageClasses = [
        CommonApplication::class
    ])
@EnableEurekaClient
@EnableScheduling
class MailServerApplication{

    @Value("\${spring.mail.host}")
    lateinit var host : String

    @Value("\${spring.mail.port}")
    var port : Int = -1

    @Value("\${spring.mail.username}")
    lateinit var username : String

    @Value("\${spring.mail.password}")
    lateinit var password : String

    @Value("\${spring.mail.properties.mail.smtp.auth}")
    lateinit var auth : String

    @Value("\${spring.mail.properties.mail.smtp.starttls.enable}")
    lateinit var enable : String

    @Value("\${spring.mail.properties.mail.debug}")
    lateinit var debug : String

    @Bean
    fun getMailSender(): JavaMailSender {

        val mailSender = JavaMailSenderImpl()
        mailSender.host = host
        mailSender.port = port
        mailSender.username = username
        mailSender.password = password

        val props: Properties = mailSender.javaMailProperties
        props["mail.transport.protocol"] = "smtp"
        props["mail.smtp.auth"] = auth
        props["mail.smtp.starttls.enable"] = enable
        props["mail.debug"] = debug

        return mailSender
    }
}

fun main(args: Array<String>){
    runApplication<MailServerApplication>(*args)
}