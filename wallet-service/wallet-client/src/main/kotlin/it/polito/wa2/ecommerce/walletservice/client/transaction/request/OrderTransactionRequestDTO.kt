package it.polito.wa2.ecommerce.walletservice.client.transaction.request

import java.math.BigDecimal
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.Digits
import javax.validation.constraints.NotNull

data class OrderTransactionRequestDTO(

    @field:NotNull(message = "WalletTo must be present")
    val warehouseTo: String,
    @field:NotNull(message = "amount must be present")
    @field:Digits(fraction=2, integer = 10)
    @field:DecimalMin(value = "0.00", inclusive = true, message = "amount should not be negative")
    val amount: BigDecimal
)
