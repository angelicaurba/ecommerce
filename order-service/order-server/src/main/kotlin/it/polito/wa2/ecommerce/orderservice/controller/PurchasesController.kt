package it.polito.wa2.ecommerce.orderservice.controller

import it.polito.wa2.ecommerce.orderservice.service.PurchasesService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/purchases")
class PurchasesController {

    @Autowired
    lateinit var purchasesService: PurchasesService

    @GetMapping("/")
    fun haveCustomerBoughtProduct(
        @RequestParam("productId") productId: String,
        @RequestParam("userId") userId: String
    ): Boolean{
        return purchasesService.haveCustomerBoughtProduct(productId,userId)
    }
}