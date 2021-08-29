package it.polito.wa2.ecommerce.orderservice.it.polito.wa2.ecommerce.orderservice.domain

import it.polito.wa2.ecommerce.common.EntityBase
import it.polito.wa2.ecommerce.orderservice.client.order.response.Status
import javax.persistence.Entity
import javax.persistence.OneToMany

@Entity
class Order(
    val buyerId: String,
    val address: String,
    val buyerWalletId: String,
    @OneToMany() // TODO params?
    val deliveryItems: List<PurchaseItem>,
    val status: Status
) : EntityBase<Long>() {
}
// TODO(add validation and columns)