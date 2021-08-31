package it.polito.wa2.ecommerce.orderservice.utils


import it.polito.wa2.ecommerce.orderservice.client.item.ItemDTO
import it.polito.wa2.ecommerce.orderservice.client.order.request.ItemsInWarehouseDTO
import it.polito.wa2.ecommerce.orderservice.domain.PurchaseItem


fun <T : ItemDTO> Set<PurchaseItem>.extractProductInWarehouse(mapper: (PurchaseItem)->T): List<ItemsInWarehouseDTO<T>>{
    return this.groupBy ({ it.warehouseId }, mapper)
        .map {
            ItemsInWarehouseDTO<T>(
                it.key,
                it.value
            ) }
}
