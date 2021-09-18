package it.polito.wa2.ecommerce.warehouseservice.client.order.request

import it.polito.wa2.ecommerce.orderservice.client.item.ItemDTO
import it.polito.wa2.ecommerce.orderservice.client.order.request.ItemsInWarehouseDTO
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

class WarehouseOrderRequestCancelDTO(
    @field:NotNull
    override val orderId: String,
    @field:NotNull @field:Size(min = 1)
    val productList: List<ItemsInWarehouseDTO<ItemDTO>>
    ) : WarehouseOrderRequestDTO {
        override val requestType = RequestType.CANCEL
    }