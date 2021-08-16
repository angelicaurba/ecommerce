package it.polito.wa2.ecommerce.walletservice.repository;

import it.polito.wa2.ecommerce.walletservice.domain.Transaction
import it.polito.wa2.ecommerce.walletservice.domain.Wallet
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional

interface TransactionRepository : PagingAndSortingRepository<Transaction, Long> {
    @Query("select t from Transaction t " +
            "where (t.fromWallet=:wallet or t.toWallet=:wallet) and t.timestamp between :startTime and :endTime"
    )
    @Transactional(readOnly = true)
    fun findByWalletInTimeRange(
        @Param("wallet") wallet: Wallet,
        @Param("startTime") startTime: Long,
        @Param("endTime") endTime: Long,
        page: Pageable
    ): List<Transaction>

    @Transactional(readOnly = true)
    fun findByFromWalletOrToWallet(walletFrom: Wallet, walletTo: Wallet, page: Pageable): List<Transaction>
}