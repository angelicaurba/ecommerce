package it.polito.wa2.ecommerce.warehouseservice.client.order.request

import it.polito.wa2.ecommerce.orderservice.client.item.PurchaseItemDTO

class WarehouseOrderRequestNewDTO(
    override val orderId: String,
    val buyerId: String,
    val buyerWalletId: String,
    val productList: List<PurchaseItemDTO>

) : WarehouseOrderRequestDTO {
    override val requestType = RequestType.NEW
}