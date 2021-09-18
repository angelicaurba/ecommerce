package it.polito.wa2.ecommerce.orderservice.client.order.request

import it.polito.wa2.ecommerce.orderservice.client.item.ItemDTO
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class ItemsInWarehouseDTO<T:ItemDTO>(
    @field:NotNull
    val warehouseId: String?,
    @field:NotNull @field:Size(min = 1)
    val purchaseItems: List<T>
)


