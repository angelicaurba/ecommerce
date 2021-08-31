package it.polito.wa2.ecommerce.orderservice.controller

import it.polito.wa2.ecommerce.common.exceptions.BadRequestException
import it.polito.wa2.ecommerce.orderservice.client.UpdateOrderRequestDTO
import it.polito.wa2.ecommerce.orderservice.client.item.PurchaseItemDTO
import it.polito.wa2.ecommerce.orderservice.client.order.request.OrderRequestDTO
import it.polito.wa2.ecommerce.orderservice.client.order.response.OrderDTO
import it.polito.wa2.ecommerce.orderservice.service.OrderService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.constraints.Min

const val DEFAULT_PAGE_SIZE = "10"

@RestController
@RequestMapping("/orders")
class OrderController {

    @Autowired lateinit var orderService: OrderService

    @GetMapping("/")
    fun getAllOrders(@RequestParam("pageIndex", defaultValue = "1") @Min(1) pageIdx: Int,
                     @RequestParam("pageSize", defaultValue = DEFAULT_PAGE_SIZE) @Min(1) pageSize: Int): List<OrderDTO>{
        return orderService.getAllOrders(pageIdx, pageSize)
    }

    @GetMapping("/{orderId}")
    fun getOrderById(@PathVariable("orderId") orderId: String): OrderDTO{
        return orderService.getOrderById(orderId)
    }

    @PostMapping("/")
    fun addOrder(@RequestBody @Valid order: OrderRequestDTO<PurchaseItemDTO>,
                 bindingResult: BindingResult
    ): OrderDTO{
        if (!bindingResult.hasErrors())
            return orderService.addOrder(order)
        throw BadRequestException("Bad order request")
    }

    @PatchMapping("/{orderId}")
    fun updateOrder(@PathVariable("orderId") orderId: String,
    @RequestBody @Valid updateOrderRequest: UpdateOrderRequestDTO,
    bindingResult : BindingResult): OrderDTO{
        if (!bindingResult.hasErrors())
            return orderService.updateStatus(orderId, updateOrderRequest)
        throw BadRequestException("Bad update request")
    }

    @DeleteMapping("/{orderId}")
    fun cancelOrder(@PathVariable("orderId") orderId: String){
        orderService.cancelOrder(orderId)
    }

}