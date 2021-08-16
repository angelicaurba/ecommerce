package it.polito.wa2.ecommerce.walletservice.client.order


//Message to be delivered once the order processing has been completed

data class OrderStatus(
    val orderID: String,
    val status: Status,
    val errorMessage: String?
)

enum class Status{
    COMPLETED, // The transaction has been correctly executed
    FAILED, // The transaction was not correctly executed
    REFUNDED // Transaction has been cancelled and refunded
}
