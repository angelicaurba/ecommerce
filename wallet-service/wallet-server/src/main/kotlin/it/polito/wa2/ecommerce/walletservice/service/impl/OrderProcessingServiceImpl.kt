package it.polito.wa2.ecommerce.walletservice.service.impl

import it.polito.wa2.ecommerce.common.parseID
import it.polito.wa2.ecommerce.walletservice.client.order.OrderStatus
import it.polito.wa2.ecommerce.walletservice.client.order.Status
import it.polito.wa2.ecommerce.walletservice.client.order.request.OrderPaymentRequestDTO
import it.polito.wa2.ecommerce.walletservice.client.order.request.OrderPaymentType
import it.polito.wa2.ecommerce.walletservice.client.order.request.OrderRequestDTO
import it.polito.wa2.ecommerce.walletservice.client.order.request.RefundRequestDTO
import it.polito.wa2.ecommerce.walletservice.domain.Transaction
import it.polito.wa2.ecommerce.walletservice.domain.TransactionType
import it.polito.wa2.ecommerce.walletservice.domain.WalletType
import it.polito.wa2.ecommerce.walletservice.repository.TransactionRepository
import it.polito.wa2.ecommerce.walletservice.repository.WalletRepository
import it.polito.wa2.ecommerce.walletservice.service.OrderProcessingService
import it.polito.wa2.ecommerce.walletservice.service.TransactionService
import it.polito.wa2.ecommerce.walletservice.service.WalletService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class OrderProcessingServiceImpl: OrderProcessingService {

    @Autowired
    lateinit var walletRepository:WalletRepository

    @Autowired
    lateinit var transactionRepository: TransactionRepository

    @Autowired
    lateinit var walletService: WalletService

    @Autowired
    lateinit var transactionService: TransactionService


    override fun processOrderRequest(orderRequestDTO: OrderRequestDTO): OrderStatus {
        //TODO manage exceptions
        val orderId = orderRequestDTO.orderId
        val walletFrom =
            walletRepository.findByIdAndWalletType(orderRequestDTO.walletFrom.parseID(), WalletType.CUSTOMER)
                ?: return OrderStatus(
                    orderId,
                    Status.FAILED,
                    "Cannot find required wallet"
                )

        if (orderRequestDTO is OrderPaymentRequestDTO) {


            for (transactionRequest in orderRequestDTO.transactionList) {
                val transaction = Transaction(
                    walletFrom,
                    walletService.getWalletOrThrowException(transactionRequest.walletTo.parseID()),
                    TransactionType.ORDER_PAYMENT,
                    System.currentTimeMillis(), //TODO should be autocreated?
                    transactionRequest.amount,
                    orderId

                )

                transactionService.processTransaction(transaction)
            }
        } else if (orderRequestDTO is RefundRequestDTO) {
            //REFUND
            val previousTransactions =
                transactionRepository.findByFromWalletAndOperationReferenceAndType(walletFrom, orderId)
            for (previousTransaction in previousTransactions) {

                val transaction = Transaction(
                    walletService.getWalletOrThrowException(previousTransaction.toWallet.getId()!!),
                    walletFrom,
                    TransactionType.ORDER_REFUND,
                    System.currentTimeMillis(), //TODO should be autocreated?
                    previousTransaction.amount,
                    orderId

                )

                transactionService.processTransaction(transaction)

            }
        }


        return OrderStatus(
            orderId, when (orderRequestDTO.requestType) {
                OrderPaymentType.PAY -> Status.COMPLETED
                OrderPaymentType.REFUND -> Status.REFUNDED
            }, null
        )


    }
}