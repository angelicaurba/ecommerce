package it.polito.wa2.ecommerce.walletservice.client.order.request

import it.polito.wa2.ecommerce.walletservice.client.transaction.request.OrderTransactionRequestDTO
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

class WalletOrderPaymentRequestDTO(
    @field:NotNull(message = "WalletFrom must be present")
    override val walletFrom: String,
    @field:NotNull(message = "UserID must be present")
    override val userId: String,
    @field:NotNull(message = "OrderID must be present")
    override val orderId: String,
    @field:NotNull(message = "List of transactions must be present") @field:Size(min=1)
    val transactionList:List<OrderTransactionRequestDTO>
): WalletOrderRequestDTO {

    override var requestType: OrderPaymentType = OrderPaymentType.PAY
}


