package it.polito.wa2.ecommerce.walletservice.service

import it.polito.wa2.ecommerce.walletservice.client.transaction.request.RechargeRequestDTO
import it.polito.wa2.ecommerce.walletservice.client.transaction.TransactionDTO
import it.polito.wa2.ecommerce.walletservice.client.wallet.request.CustomerWalletCreationRequestDTO
import it.polito.wa2.ecommerce.walletservice.client.wallet.request.WalletCreationRequestDTO
import it.polito.wa2.ecommerce.walletservice.client.wallet.WalletDTO
import it.polito.wa2.ecommerce.walletservice.client.wallet.request.WarehouseWalletCreationRequestDTO
import it.polito.wa2.ecommerce.walletservice.domain.Transaction
import it.polito.wa2.ecommerce.walletservice.domain.TransactionType
import it.polito.wa2.ecommerce.walletservice.domain.Wallet
import it.polito.wa2.ecommerce.walletservice.domain.WalletType
import it.polito.wa2.ecommerce.walletservice.repository.TransactionRepository
import it.polito.wa2.ecommerce.walletservice.repository.WalletRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*
import javax.transaction.Transactional

@Service
@Transactional
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
        //TODO add check if userID == principal.userID  or is admin
        val wallet = walletCreationRequest.toEntity()

        if(walletCreationRequest is WarehouseWalletCreationRequestDTO &&
            walletRepository.findByWalletTypeAndOwner(WalletType.WAREHOUSE,walletCreationRequest.warehouseID )!=null)
            //TODO is it ok that constraint?
            throw Exception("Warehouses cannot have more than one wallet") //TODO change exception

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
        //TODO should go in transactionService ?
        val page = getPageable(pageIdx, pageSize)
        return transactionRepository.findByFromWalletOrToWallet(wallet, wallet, page).map { it.toDTO() }
    }

    override fun getTransactionByWalletIdAndTransactionId(walletId: String, transactionId: String): TransactionDTO {
        val walletIdLong = walletId.parseID();
        getWalletOrThrowException(walletIdLong)
        val transaction = getTransactionOrThrowException(transactionId.parseID())
        if (transaction.fromWallet?.getId() != walletIdLong && transaction.toWallet.getId() != walletIdLong) {
//        TODO    throw TransactionNotFound("Cannot find transaction with ID $transactionId for wallet $walletId")
            throw Exception("Cannot find transaction with ID $transactionId for wallet $walletId")
        }
        //TODO should go in transactionService ?
        return transaction.toDTO()
    }

    override fun rechargeWallet(walletId: String, rechargeRequestDTO: RechargeRequestDTO): TransactionDTO {
        //TODO verify admin role
        //TODO should go in transactionService ?
        val wallet = getWalletOrThrowException(walletId.parseID())
        wallet.amount+=rechargeRequestDTO.amount
        val transaction = Transaction(null, wallet, TransactionType.RECHARGE,
            System.currentTimeMillis(), rechargeRequestDTO.amount, UUID.randomUUID().toString() )


        walletRepository.save(wallet)
        return transactionRepository.save(transaction).toDTO()

    }

    private inline fun getWalletOrThrowException(walletId: Long): Wallet {
        return walletRepository.findByIdOrNull(walletId) ?: throw RuntimeException()
//        TODO throw WalletNotFound(walletId)
    }

    private inline fun getTransactionOrThrowException(walletId: Long): Transaction {
        return transactionRepository.findByIdOrNull(walletId) ?: throw RuntimeException()
//        TODO throw TransactionNotFound(walletId)
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