package it.polito.wa2.ecommerce.orderservice.it.polito.wa2.ecommerce.orderservice.controller

import it.polito.wa2.ecommerce.orderservice.client.OrderDTO
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/orders")
class OrderController {

    @GetMapping("/")
    fun getAllOrders(): List<OrderDTO>{
        TODO("implement")
    }
}