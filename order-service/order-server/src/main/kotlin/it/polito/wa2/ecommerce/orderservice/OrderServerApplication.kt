package it.polito.wa2.ecommerce.orderservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["it.polito.wa2.ecommerce"])
class OrderServerApplication

fun main(args: Array<String>) {
    runApplication<OrderServerApplication>(*args)
}