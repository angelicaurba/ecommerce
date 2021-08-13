package it.polito.wa2.ecommerce.walletservice.client.wallet

import java.math.BigDecimal

class CustomerWalletDTO(
    override val walletId: String,
    val userId:String,
    override val amount: BigDecimal) : WalletDTO {
}