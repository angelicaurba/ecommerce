package it.polito.wa2.ecommerce.warehouseservice.service.impl

import it.polito.wa2.ecommerce.warehouseservice.service.OrderProcessingService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class OrderProcessingServiceImpl: OrderProcessingService {
    override fun process(orderRequestDTO: Any, id: String) {
        TODO("Not yet implemented")
    }
}