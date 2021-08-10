package it.polito.wa2.ecommerce.walletservice.client.transaction


//Message to be delivered once the order processing has been completed

data class TransactionStatus(
    val orderID: String,
    val status: Status
)

enum class Status{
    COMPLETED, // The transaction has been correctly executed
    FAILED, // The transaction was not correctly executed
    REFUNDED // Transaction has been cancelled and refunded
}
