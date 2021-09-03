package it.polito.wa2.ecommerce.walletservice.service.impl

import it.polito.wa2.ecommerce.common.parseID
import it.polito.wa2.ecommerce.common.saga.service.MessageService
import it.polito.wa2.ecommerce.common.saga.service.ProcessingLogService
import it.polito.wa2.ecommerce.orderservice.client.order.messages.EventTypeOrderStatus
import it.polito.wa2.ecommerce.orderservice.client.order.messages.OrderStatus
import it.polito.wa2.ecommerce.orderservice.client.order.messages.ResponseStatus
import it.polito.wa2.ecommerce.walletservice.client.order.request.WalletOrderPaymentRequestDTO
import it.polito.wa2.ecommerce.walletservice.client.order.request.OrderPaymentType
import it.polito.wa2.ecommerce.walletservice.client.order.request.WalletOrderRequestDTO
import it.polito.wa2.ecommerce.walletservice.client.order.request.WalletOrderRefundRequestDTO
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
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(propagation = Propagation.NESTED)
class OrderProcessingServiceImpl: OrderProcessingService {

    @Autowired
    lateinit var walletRepository:WalletRepository

    @Autowired
    lateinit var transactionRepository: TransactionRepository

    @Autowired
    lateinit var walletService: WalletService

    @Autowired
    lateinit var transactionService: TransactionService

    @Autowired
    lateinit var processingLogService: ProcessingLogService

    @Autowired
    lateinit var messageService: MessageService

    @Autowired
    lateinit var self: OrderProcessingService

    override fun process(orderRequestDTO: WalletOrderRequestDTO, id: String){
        val uuid = UUID.fromString(id)
        if(processingLogService.isProcessed(uuid))
            return

        var status: OrderStatus? = null
        try {
            status = self.processOrderRequest(orderRequestDTO)
        }
        catch (e:Exception){
            status = OrderStatus(
                orderRequestDTO.orderId,
                ResponseStatus.FAILED,
                e.message)
        }
        finally {
            processingLogService.process(uuid)
            status?.also {
                messageService.publish(it,
                    if(it.responseStatus == ResponseStatus.COMPLETED)
                        EventTypeOrderStatus.OrderOk.toString()
                    else EventTypeOrderStatus.OrderPaymentFailed.toString(),
                    "order-status") //TODO define constant
            }
        }
    }


    override fun processOrderRequest(orderRequestDTO: WalletOrderRequestDTO): OrderStatus? {
        val orderId = orderRequestDTO.orderId
        val walletFrom =
            walletRepository.findByIdAndWalletType(orderRequestDTO.walletFrom.parseID(), WalletType.CUSTOMER)
                ?: return OrderStatus(
                    orderId,
                    ResponseStatus.FAILED,
                    "Cannot find required wallet"
                )

        if (orderRequestDTO is WalletOrderPaymentRequestDTO) {
            for (transactionRequest in orderRequestDTO.transactionList) {
                val transaction = Transaction(
                    walletFrom,
                    walletService.getWalletOrThrowException(transactionRequest.walletTo.parseID()),
                    TransactionType.ORDER_PAYMENT,
                    transactionRequest.amount,
                    orderId

                )

                transactionService.processTransaction(transaction)
            }

            return OrderStatus(
                orderId,
                ResponseStatus.COMPLETED,
                null
            )
        } else if (orderRequestDTO is WalletOrderRefundRequestDTO) {
            //REFUND
            val previousTransactions =
                transactionRepository.findByFromWalletAndOperationReferenceAndType(walletFrom, orderId)
            for (previousTransaction in previousTransactions) {

                val transaction = Transaction(
                    walletService.getWalletOrThrowException(previousTransaction.toWallet!!.getId()!!),
                    walletFrom,
                    TransactionType.ORDER_REFUND,
                    previousTransaction.amount,
                    orderId

                )

                transactionService.processTransaction(transaction)

            }

        }
        return null
    }
}