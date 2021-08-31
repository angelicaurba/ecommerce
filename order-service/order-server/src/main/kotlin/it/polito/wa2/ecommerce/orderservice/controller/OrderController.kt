package it.polito.wa2.ecommerce.orderservice.controller

import it.polito.wa2.ecommerce.orderservice.client.order.response.OrderDTO
import it.polito.wa2.ecommerce.orderservice.service.OrderService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.validation.constraints.Min

const val DEFAULT_PAGE_SIZE = "10"

@RestController
@RequestMapping("/orders")
class OrderController {

    @GetMapping("/")
    fun getAllOrders(@RequestParam("pageIndex", defaultValue = "1") @Min(1) pageIdx: Int,
                     @RequestParam("pageSize", defaultValue = DEFAULT_PAGE_SIZE) @Min(1) pageSize: Int): List<OrderDTO>{
        TODO("implement")
    }
}