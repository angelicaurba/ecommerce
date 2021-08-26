package it.polito.wa2.ecommerce.walletservice.service

import it.polito.wa2.ecommerce.walletservice.client.transaction.TransactionDTO
import it.polito.wa2.ecommerce.walletservice.client.transaction.request.RechargeRequestDTO
import it.polito.wa2.ecommerce.walletservice.domain.Transaction

interface TransactionService {

    fun getTransactionsByWalletIdAndTimeInterval(walletId: String, startTime: Long, endTime: Long, pageIdx: Int, pageSize: Int): List<TransactionDTO>
    fun getTransactionsByWalletId(walletId: String, pageIdx: Int, pageSize: Int): List<TransactionDTO>
    fun getTransactionByWalletIdAndTransactionId(walletId: String, transactionId: String): TransactionDTO
    fun rechargeWallet(walletId: String, rechargeRequestDTO: RechargeRequestDTO): TransactionDTO
    fun processTransaction(transaction: Transaction): Transaction
}