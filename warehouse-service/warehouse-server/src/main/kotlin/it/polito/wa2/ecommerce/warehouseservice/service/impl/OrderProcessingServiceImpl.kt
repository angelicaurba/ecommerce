package it.polito.wa2.ecommerce.warehouseservice.service.impl

import it.polito.wa2.ecommerce.orderservice.client.order.messages.OrderStatus
import it.polito.wa2.ecommerce.warehouseservice.client.order.request.WarehouseOrderRequestDTO
import it.polito.wa2.ecommerce.warehouseservice.service.OrderProcessingService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class OrderProcessingServiceImpl: OrderProcessingService {
    // TODO commented function in order to make the project compile
    // The original "process" override is commented,
    // and empty override for all the functions are added

    //    override fun process(orderRequestDTO: Any, id: String) {
//        TODO("Not yet implemented")
//    }
    override fun process(orderRequestDTO: WarehouseOrderRequestDTO, id: String) {
        TODO("Not yet implemented")
    }

    override fun processOrderRequest(orderRequestDTO: WarehouseOrderRequestDTO): OrderStatus {
        TODO("Not yet implemented")
    }

}