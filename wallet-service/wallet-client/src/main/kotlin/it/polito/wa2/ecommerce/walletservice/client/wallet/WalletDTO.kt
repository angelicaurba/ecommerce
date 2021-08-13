package it.polito.wa2.ecommerce.walletservice.client.wallet

import java.math.BigDecimal

interface WalletDTO {
    val walletId:String
    val amount:BigDecimal
}
