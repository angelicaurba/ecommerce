package it.polito.wa2.ecommerce.walletservice.client

import java.math.BigDecimal

//TODO add validation
data class RechargeRequestDTO(
    val amount:BigDecimal
)
