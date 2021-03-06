package it.polito.wa2.ecommerce.walletservice.client.wallet.request

import javax.validation.constraints.NotNull

class WarehouseWalletCreationRequestDTO (
    @field:NotNull(message = "Warehouse must be present")
    val warehouseID:String): WalletCreationRequestDTO {

    override val walletType= "WAREHOUSE"

    override fun getId(): String {
        return warehouseID
    }
}