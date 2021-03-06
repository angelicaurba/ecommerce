package it.polito.wa2.ecommerce.walletservice.client.wallet

import java.math.BigDecimal

class WarehouseWalletDTO(
    override val walletId: String,
    val warehouseId:String,
    override val amount: BigDecimal) : WalletDTO {
}