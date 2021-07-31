package it.polito.wa2.ecommerce.orderservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class OrderServerApplication

fun main(args: Array<String>) {
    runApplication<OrderServerApplication>(*args)
}