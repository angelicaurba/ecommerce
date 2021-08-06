package it.polito.wa2.ecommerce.walletservice.service

import it.polito.wa2.ecommerce.walletservice.client.*
import it.polito.wa2.ecommerce.walletservice.domain.Wallet
import it.polito.wa2.ecommerce.walletservice.domain.WalletType
import it.polito.wa2.ecommerce.walletservice.repository.TransactionRepository
import it.polito.wa2.ecommerce.walletservice.repository.WalletRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class WalletServiceImpl:WalletService {

    @Autowired
    lateinit var walletRepository: WalletRepository

    @Autowired
    lateinit var transactionRepository:TransactionRepository

    fun String.parseID(): Long{
        return this.toLongOrNull() ?: throw RuntimeException("Not found") // TODO change exception type
    }

    override fun getWalletById(id:String): WalletDTO {

        val idInt = id.parseID()
        return walletRepository.findByIdOrNull(idInt)?.toDTO() ?: throw RuntimeException() //TODO change exception type


    }

    override fun addWallet(walletCreationRequest: WalletCreationRequestDTO): WalletDTO {
        val wallet = walletCreationRequest.toEntity()

        return walletRepository.save(wallet).toDTO()
    }

    override fun deleteWallet(walletId: String) {
        val id = walletId.parseID()
        //TODO verify that is the owner (or admin to delete the wallet)
        walletRepository.deleteById(id)
    }

    override fun getTransactionsByWalletIdAndTimeInterval(
        walletId: String,
        startTime: Long,
        endTime: Long,
        pageIdx: Int,
        pageSize: Int
    ): List<TransactionDTO> {
        if ( startTime > endTime ) {
            throw RuntimeException("Invalid time range") //TODO change request type
        }
        val wallet = getWalletOrThrowException(walletId.parseID())

        //TODO should go in transactionService ?
        val page = getPageable(pageIdx, pageSize)

        return transactionRepository.findByWalletInTimeRange(wallet, startTime, endTime,page).map { it.toDTO() }
    }

    override fun getTransactionsByWalletId(walletId: String, pageIdx: Int, pageSize: Int): List<TransactionDTO>{
        val wallet = getWalletOrThrowException(walletId.parseID())

        val page = getPageable(pageIdx, pageSize)
        return transactionRepository.findByFromWalletOrToWallet(wallet, wallet, page).map { it.toDTO() }
    }

    private inline fun getWalletOrThrowException(walletId: Long): Wallet {
        return walletRepository.findByIdOrNull(walletId) ?: throw RuntimeException()
//        TODO throw WalletNotFound(walletId)
    }


    private fun getPageable(pageIdx: Int, pageSize: Int) =
        PageRequest.of(
            pageIdx - 1, // pageable index starts from 0, while API have indexes starting from 1
            pageSize)
}

fun WalletCreationRequestDTO.toEntity(): Wallet{
    return when (this) {
        is WarehouseWalletCreationRequestDTO -> Wallet(this.warehouseID, WalletType.WAREHOUSE)
        is CustomerWalletCreationRequestDTO -> Wallet(this.customerID, WalletType.CUSTOMER)
        else -> throw RuntimeException("Request type is not recognised") //TODO study exception type
    }
}