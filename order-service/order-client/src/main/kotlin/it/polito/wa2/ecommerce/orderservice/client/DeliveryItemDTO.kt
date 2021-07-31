package it.polito.wa2.ecommerce.orderservice.client

data class DeliveryItemDTO(
    val warehouseId: Long,
    val purchaseItems: List<ItemDTO>
)
