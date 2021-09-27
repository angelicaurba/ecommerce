package it.polito.wa2.ecommerce.walletservice.service

import it.polito.wa2.ecommerce.walletservice.client.wallet.request.WalletCreationRequestDTO
import it.polito.wa2.ecommerce.walletservice.client.wallet.WalletDTO
import it.polito.wa2.ecommerce.walletservice.domain.Wallet
import it.polito.wa2.ecommerce.walletservice.domain.WalletType

interface WalletService {

    fun getWalletById(id:String): WalletDTO
    fun addWallet(walletCreationRequest: WalletCreationRequestDTO, verifySecurity:Boolean = true): WalletDTO
    fun getWalletOrThrowException(walletId: Long, verifySecurity: Boolean = true): Wallet
    fun getAllWallets(ownerId: String?, walletType: String?, pageIdx: Int, pageSize: Int): List<WalletDTO>
}