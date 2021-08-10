package it.polito.wa2.ecommerce.walletservice.client.transaction.request

import javax.validation.constraints.NotNull

data class OrderPaymentRequestDTO(
    @field:NotNull(message = "WalletFrom must be present")
    val walletFrom: String,
    @field:NotNull(message = "UserID must be present")
    val userId: String,
    @field:NotNull(message = "OrderID must be present")
    val orderId: String,
    @field:NotNull(message = "Type must be present")
    val requestType: OrderPaymentType,
    val transactionList:List<OrderTransactionRequestDTO>
)

enum class OrderPaymentType{
    PAY,
    REFUND
}
