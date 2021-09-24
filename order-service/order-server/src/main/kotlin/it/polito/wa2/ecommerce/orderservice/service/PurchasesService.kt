package it.polito.wa2.ecommerce.orderservice.service

interface PurchasesService {
    fun haveCustomerBoughtProduct(productId:String, userId: String):Boolean
}