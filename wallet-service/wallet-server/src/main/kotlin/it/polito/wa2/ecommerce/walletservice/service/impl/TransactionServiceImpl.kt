package it.polito.wa2.ecommerce.walletservice.service.impl

import it.polito.wa2.ecommerce.common.exceptions.BadRequestException
import it.polito.wa2.ecommerce.common.getPageable
import it.polito.wa2.ecommerce.common.parseID
import it.polito.wa2.ecommerce.common.security.IdentityVerifier
import it.polito.wa2.ecommerce.walletservice.client.transaction.TransactionDTO
import it.polito.wa2.ecommerce.walletservice.client.transaction.request.RechargeRequestDTO
import it.polito.wa2.ecommerce.walletservice.domain.Transaction
import it.polito.wa2.ecommerce.walletservice.domain.TransactionType
import it.polito.wa2.ecommerce.walletservice.domain.WalletType
import it.polito.wa2.ecommerce.walletservice.exception.OutOfMoneyException
import it.polito.wa2.ecommerce.walletservice.exception.TransactionNotFoundException
import it.polito.wa2.ecommerce.walletservice.repository.TransactionRepository
import it.polito.wa2.ecommerce.walletservice.repository.WalletRepository
import it.polito.wa2.ecommerce.walletservice.service.TransactionService
import it.polito.wa2.ecommerce.walletservice.service.WalletService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class TransactionServiceImpl : TransactionService {

    @Autowired
    private lateinit var transactionRepository: TransactionRepository

    @Autowired
    private lateinit var walletService: WalletService

    @Autowired
    private lateinit var walletRepository: WalletRepository

    @Autowired
    lateinit var identityVerifier: IdentityVerifier

    override fun getTransactionsByWalletIdAndTimeInterval(
        walletId: String,
        startTime: Long,
        endTime: Long,
        pageIdx: Int,
        pageSize: Int
    ): List<TransactionDTO> {
        if (startTime > endTime) {
            throw BadRequestException("Invalid time range")
        }
        val wallet = walletService.getWalletOrThrowException(walletId.parseID())
        val page = getPageable(pageIdx, pageSize)

        return transactionRepository.findByWalletInTimeRange(wallet, startTime, endTime, page).map { it.toDTO() }
    }

    override fun getTransactionsByWalletId(walletId: String, pageIdx: Int, pageSize: Int): List<TransactionDTO> {
        val wallet = walletService.getWalletOrThrowException(walletId.parseID())
        val page = getPageable(pageIdx, pageSize)
        return transactionRepository.findByFromWalletOrToWallet(wallet, wallet, page).map { it.toDTO() }
    }

    override fun getTransactionByWalletIdAndTransactionId(walletId: String, transactionId: String): TransactionDTO {
        val walletIdLong = walletId.parseID()
        walletService.getWalletOrThrowException(walletIdLong)
        val transaction = getTransactionOrThrowException(transactionId.parseID())
        if (transaction.fromWallet?.getId() != walletIdLong && transaction.toWallet!!.getId() != walletIdLong) {
            throw TransactionNotFoundException("Cannot find transaction with ID $transactionId for wallet $walletId")
        }
        return transaction.toDTO()
    }

    @PreAuthorize("hasAuthority(T(it.polito.wa2.ecommerce.common.Rolename).ADMIN)")
    override fun rechargeWallet(walletId: String, rechargeRequestDTO: RechargeRequestDTO): TransactionDTO {
        val wallet = walletService.getWalletOrThrowException(walletId.parseID())
        val transaction = Transaction(
            null, wallet, TransactionType.RECHARGE,
            rechargeRequestDTO.amount, UUID.randomUUID().toString()
        )

        return processTransaction(transaction).toDTO()


    }

    fun getTransactionOrThrowException(transactionId: Long): Transaction {
        return transactionRepository.findByIdOrNull(transactionId) ?: throw TransactionNotFoundException(transactionId)
    }

    override fun processTransaction(
        transaction: Transaction,
    ): Transaction {
        val amount = transaction.amount
        when (transaction.type) {
            TransactionType.ORDER_PAYMENT -> {
                if (transaction.fromWallet!!.amount < amount) {
                    throw  OutOfMoneyException("Order cannot be processed")
                }
                if (transaction.toWallet!!.walletType != WalletType.WAREHOUSE) {
                    throw BadRequestException("Wallet to must belong to a warehouse for orders")
                }
                transaction.fromWallet!!.amount = transaction.fromWallet!!.amount.minus(amount)
                transaction.toWallet!!.amount = transaction.toWallet!!.amount.plus(amount)


            }
            TransactionType.ORDER_REFUND -> {
                if (transaction.fromWallet!!.walletType != WalletType.WAREHOUSE) {
                    throw BadRequestException("Refund must have a wallet from belonging to a warehouse")
                }

                // no check on available amount -> admitting negative warehouse wallet
                transaction.fromWallet!!.amount = transaction.fromWallet!!.amount.minus(amount)
                transaction.toWallet!!.amount = transaction.toWallet!!.amount.plus(amount)

            }
            TransactionType.RECHARGE -> {
                if (transaction.fromWallet != null)
                    throw BadRequestException("No field fromWallet in recharges")

                transaction.toWallet!!.amount = transaction.toWallet!!.amount.plus(amount)

            }
        }

        transaction.fromWallet?.apply {
            walletRepository.save(this)
        }
        walletRepository.save(transaction.toWallet!!)

        return transactionRepository.save(transaction)

    }

}