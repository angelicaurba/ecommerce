package it.polito.wa2.ecommerce.walletservice.repository

import it.polito.wa2.ecommerce.walletservice.domain.Wallet
import it.polito.wa2.ecommerce.walletservice.domain.WalletType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface WalletRepository: PagingAndSortingRepository<Wallet, Long> {

    @Transactional(readOnly = true)
    fun findByWalletTypeAndOwner(walletType: WalletType, owner: String): Wallet?
    @Transactional(readOnly = true)
    fun findByIdAndOwnerAndWalletType(id:Long, owner: String, walletType: WalletType):Wallet?

    @Transactional(readOnly = true)
    fun findByOwnerAndWalletType(owner: String?, walletType: WalletType?, page: Pageable): Page<Wallet>


}