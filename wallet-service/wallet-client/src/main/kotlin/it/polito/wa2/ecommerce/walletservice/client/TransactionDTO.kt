package it.polito.wa2.ecommerce.walletservice.client

import java.math.BigDecimal


//TODO define inheritance to include recharges?
data class TransactionDTO(
    val id: Long,
    val fromWallet: String,
    val toWallet: String,
    val amount: BigDecimal,
    val timestamp: Long
)
