package it.polito.wa2.ecommerce.walletservice.client.wallet.request

import javax.validation.constraints.NotNull

class CustomerWalletCreationRequestDTO (
    @field:NotNull(message = "Customer must be present")
    val customerID:String): WalletCreationRequestDTO {

    override val walletType = "CUSTOMER"

    override fun getId(): String {
        return customerID
    }
}