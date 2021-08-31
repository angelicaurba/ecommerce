package it.polito.wa2.ecommerce.walletservice.repository

import it.polito.wa2.ecommerce.walletservice.domain.Wallet
import it.polito.wa2.ecommerce.walletservice.domain.WalletType
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface WalletRepository: PagingAndSortingRepository<Wallet, Long> {

    @Transactional(readOnly = true)
    fun findByWalletTypeAndOwner(walletType: WalletType, owner: String): Wallet?
    @Transactional(readOnly = true)
    fun findByIdAndWalletType(id:Long, walletType: WalletType):Wallet?


}