package it.polito.wa2.ecommerce.orderservice.client.order.request

import it.polito.wa2.ecommerce.orderservice.client.item.ItemDTO

data class ItemsInWarehouseDTO<T:ItemDTO>(
    val warehouseId: String,
    val purchaseItems: List<T>
)
// TODO(add validation)
