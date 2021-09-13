package it.polito.wa2.ecommerce.walletservice.client.transaction

import java.math.BigDecimal
import java.time.LocalDateTime

data class TransactionDTO(
    val id: Long,
    val fromWallet: String?,
    val toWallet: String,
    val amount: BigDecimal,
    val timestamp: LocalDateTime,
    val reason:String
)
