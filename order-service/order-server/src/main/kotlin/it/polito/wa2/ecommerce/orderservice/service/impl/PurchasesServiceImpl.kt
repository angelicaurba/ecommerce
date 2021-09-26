package it.polito.wa2.ecommerce.orderservice.service.impl

import it.polito.wa2.ecommerce.orderservice.repository.PurchaseItemRepository
import it.polito.wa2.ecommerce.orderservice.service.PurchasesService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PurchasesServiceImpl: PurchasesService {

    @Autowired
    lateinit var purchaseItemRepository: PurchaseItemRepository

    override fun haveCustomerBoughtProduct(productId: String, userId: String):Boolean {
        return purchaseItemRepository.haveBuyerIdBoughtProduct(productId, userId)
    }
}