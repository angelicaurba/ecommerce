package it.polito.wa2.ecommerce.walletservice.client.transaction.request

import java.math.BigDecimal
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.NotNull

data class RechargeRequestDTO(
    @field:NotNull(message = "amount must be present")
    @field:DecimalMin(value = "0.00", inclusive = true, message = "amount should not be negative")
    val amount:BigDecimal
)
