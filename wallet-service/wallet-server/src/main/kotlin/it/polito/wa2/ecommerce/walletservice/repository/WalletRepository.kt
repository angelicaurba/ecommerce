package it.polito.wa2.ecommerce.walletservice.repository

import it.polito.wa2.ecommerce.walletservice.domain.Wallet
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface WalletRepository: PagingAndSortingRepository<Wallet, Long> {

}