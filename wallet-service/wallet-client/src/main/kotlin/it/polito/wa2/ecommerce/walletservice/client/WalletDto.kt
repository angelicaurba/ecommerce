package it.polito.wa2.ecommerce.walletservice.client

import java.math.BigDecimal

interface WalletDTO {
    val walletId:String
    val amount:BigDecimal
}
