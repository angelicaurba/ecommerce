package it.polito.wa2.ecommerce.walletservice.client

import javax.validation.constraints.NotNull

class CustomerWalletCreationRequestDTO (
    @field:NotNull(message = "Customer must be present")
    val customerID:String):WalletCreationRequestDTO