package it.polito.wa2.ecommerce.orderservice.client.order.messages

import it.polito.wa2.ecommerce.common.saga.domain.Emittable

//Message to be delivered once the order processing has been completed

data class OrderStatus(
    val orderID: String,
    val status: Status,
    val errorMessage: String?
):Emittable {
    override fun getId(): String {
        return orderID
    }
}

enum class Status{
    COMPLETED, // The transaction has been correctly executed
    FAILED, // The transaction was not correctly executed
    REFUNDED // Transaction has been cancelled and refunded
}
