package it.polito.wa2.ecommerce.walletservice.client.order.request

import javax.validation.constraints.NotNull

class OrderRefundRequestDTO (
    @field:NotNull(message = "WalletFrom must be present")
    override val walletFrom: String,
    @field:NotNull(message = "UserID must be present")
    override val userId: String,
    @field:NotNull(message = "OrderID must be present")
    override val orderId: String
    ): OrderRequestDTO{

    override val requestType: OrderPaymentType = OrderPaymentType.REFUND

    override fun getId(): String {
        return orderId
    }

}
