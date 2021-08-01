package it.polito.wa2.ecommerce.walletservice.client

import java.math.BigDecimal

data class TransactionDTO(
    val id: Long,
    val fromWallet: String?,
    val toWallet: String,
    val amount: BigDecimal,
    val timestamp: Long,
    val reason:String
)
