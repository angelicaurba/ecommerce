package it.polito.wa2.ecommerce.walletservice.service

import it.polito.wa2.ecommerce.walletservice.client.order.OrderStatus
import it.polito.wa2.ecommerce.walletservice.client.transaction.request.RechargeRequestDTO
import it.polito.wa2.ecommerce.walletservice.client.transaction.TransactionDTO
import it.polito.wa2.ecommerce.walletservice.client.order.request.OrderRequestDTO
import it.polito.wa2.ecommerce.walletservice.client.wallet.request.WalletCreationRequestDTO
import it.polito.wa2.ecommerce.walletservice.client.wallet.WalletDTO
import it.polito.wa2.ecommerce.walletservice.domain.Wallet

interface WalletService {

    fun getWalletById(id:String): WalletDTO
    fun addWallet(walletCreationRequest: WalletCreationRequestDTO): WalletDTO
    fun deleteWallet(walletId: String)
    fun getWalletOrThrowException(walletId: Long): Wallet
}