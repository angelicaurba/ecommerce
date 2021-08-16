package it.polito.wa2.ecommerce.walletservice.client.order.request

import it.polito.wa2.ecommerce.walletservice.client.transaction.request.OrderTransactionRequestDTO
import javax.validation.constraints.NotNull

class OrderPaymentRequestDTO(
    @field:NotNull(message = "WalletFrom must be present")
    override val walletFrom: String,
    @field:NotNull(message = "UserID must be present")
    override val userId: String,
    @field:NotNull(message = "OrderID must be present")
    override val orderId: String,
    @field:NotNull(message = "Type must be present")
    override val requestType: OrderPaymentType,
    @field:NotNull(message = "List of transactions must be present")
    val transactionList:List<OrderTransactionRequestDTO>
): OrderRequestDTO


