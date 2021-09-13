package it.polito.wa2.ecommerce.walletservice.service

import it.polito.wa2.ecommerce.walletservice.client.wallet.request.WalletCreationRequestDTO
import it.polito.wa2.ecommerce.walletservice.client.wallet.WalletDTO
import it.polito.wa2.ecommerce.walletservice.domain.Wallet

interface WalletService {

    fun getWalletById(id:String): WalletDTO
    fun addWallet(walletCreationRequest: WalletCreationRequestDTO, verifySecurity:Boolean = true): WalletDTO
    fun getWalletOrThrowException(walletId: Long): Wallet
}