package it.polito.wa2.ecommerce.walletservice.service

import it.polito.wa2.ecommerce.walletservice.client.RechargeRequestDTO
import it.polito.wa2.ecommerce.walletservice.client.TransactionDTO
import it.polito.wa2.ecommerce.walletservice.client.WalletCreationRequestDTO
import it.polito.wa2.ecommerce.walletservice.client.WalletDTO

interface WalletService {

    fun getWalletById(id:String): WalletDTO
    fun addWallet(walletCreationRequest: WalletCreationRequestDTO): WalletDTO
    fun deleteWallet(walletId: String)
    fun getTransactionsByWalletIdAndTimeInterval(walletId: String, startTime: Long, endTime: Long, pageIdx: Int, pageSize: Int): List<TransactionDTO>
    fun getTransactionsByWalletId(walletId: String, pageIdx: Int, pageSize: Int): List<TransactionDTO>
    fun getTransactionByWalletIdAndTransactionId(walletId: String, transactionId: String): TransactionDTO
    fun rechargeWallet(walletId: String, rechargeRequestDTO: RechargeRequestDTO): TransactionDTO
}