package it.polito.wa2.ecommerce.warehouseservice.client.order.request

import it.polito.wa2.ecommerce.orderservice.client.item.ItemDTO
import it.polito.wa2.ecommerce.orderservice.client.order.request.ItemsInWarehouseDTO

class WarehouseOrderRequestCancelDTO(
    override val orderId: String,
    val productList: List<ItemsInWarehouseDTO<ItemDTO>>
    ) : WarehouseOrderRequestDTO {
        override val requestType = RequestType.CANCEL
    }