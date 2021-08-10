package it.polito.wa2.ecommerce.walletservice.repository

import it.polito.wa2.ecommerce.walletservice.domain.Wallet
import it.polito.wa2.ecommerce.walletservice.domain.WalletType
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface WalletRepository: PagingAndSortingRepository<Wallet, Long> {

    fun findByWalletTypeAndOwner(walletType: WalletType, owner: String): Wallet?

}