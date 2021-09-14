package it.polito.wa2.ecommerce.orderservice.client.order.request

import it.polito.wa2.ecommerce.orderservice.client.item.ItemDTO
import javax.validation.constraints.NotNull

data class ItemsInWarehouseDTO<T:ItemDTO>(
    val warehouseId: String?,
    @field:NotNull
    val purchaseItems: List<T>
)


