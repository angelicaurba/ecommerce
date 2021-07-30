package it.polito.wa2.ecommerce.walletservice.controller

import it.polito.wa2.ecommerce.walletservice.client.WalletDto

class WalletController {

    fun sample(): WalletDto {
        return WalletDto("1", "2")
    }
}