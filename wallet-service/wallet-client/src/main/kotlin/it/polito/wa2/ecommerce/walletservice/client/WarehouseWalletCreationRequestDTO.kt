package it.polito.wa2.ecommerce.walletservice.client

import javax.validation.constraints.NotNull

class WarehouseWalletCreationRequestDTO (
    @field:NotNull(message = "Warehouse must be present")
    val warehouseID:String):WalletCreationRequestDTO