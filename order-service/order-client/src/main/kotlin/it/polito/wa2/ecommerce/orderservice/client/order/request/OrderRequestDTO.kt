package it.polito.wa2.ecommerce.orderservice.client.order.request

import it.polito.wa2.ecommerce.orderservice.client.item.ItemDTO
import org.jetbrains.annotations.NotNull
import javax.validation.constraints.Size

data class OrderRequestDTO<T: ItemDTO>(
    @field:NotNull
    val buyerId: String, // this field is needed since an admin may request an order for a customer in some special case
    @field:NotNull
    val buyerWalletId: String,
    @field:NotNull
    val address: String,
    @field:NotNull @field:Size(min=1)
    val deliveryItems: List<T>
)