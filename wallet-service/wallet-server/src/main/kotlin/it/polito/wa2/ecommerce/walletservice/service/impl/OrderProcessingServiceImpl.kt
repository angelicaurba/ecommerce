package it.polito.wa2.ecommerce.walletservice.service.impl

import it.polito.wa2.ecommerce.common.parseID
import it.polito.wa2.ecommerce.common.saga.service.MessageService
import it.polito.wa2.ecommerce.common.saga.service.ProcessingLogService
import it.polito.wa2.ecommerce.orderservice.client.order.messages.OrderStatus
import it.polito.wa2.ecommerce.orderservice.client.order.messages.Status
import it.polito.wa2.ecommerce.walletservice.client.order.request.WarehouseOrderPaymentRequestDTO
import it.polito.wa2.ecommerce.walletservice.client.order.request.OrderPaymentType
import it.polito.wa2.ecommerce.walletservice.client.order.request.WarehouseOrderRequestDTO
import it.polito.wa2.ecommerce.walletservice.client.order.request.WarehouseOrderRefundRequestDTO
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

    override fun process(orderRequestDTO: WarehouseOrderRequestDTO, id: String){
        val uuid = UUID.fromString(id)
        if(processingLogService.isProcessed(uuid))
            return

        lateinit var status: OrderStatus
        try {
            status = self.processOrderRequest(orderRequestDTO)
        }
        catch (e:Exception){
            status = OrderStatus(
                orderRequestDTO.orderId,
                Status.FAILED,
                e.message)
        }
        finally {
            processingLogService.process(uuid)
            messageService.publish(status, "ORDER-${status.status}", "order") //TODO define name
        }
    }


    override fun processOrderRequest(orderRequestDTO: WarehouseOrderRequestDTO): OrderStatus {
        val orderId = orderRequestDTO.orderId
        val walletFrom =
            walletRepository.findByIdAndWalletType(orderRequestDTO.walletFrom.parseID(), WalletType.CUSTOMER)
                ?: return OrderStatus(
                    orderId,
                    Status.FAILED,
                    "Cannot find required wallet"
                )

        if (orderRequestDTO is WarehouseOrderPaymentRequestDTO) {


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
        } else if (orderRequestDTO is WarehouseOrderRefundRequestDTO) {
            //REFUND
            val previousTransactions =
                transactionRepository.findByFromWalletAndOperationReferenceAndType(walletFrom, orderId)
            for (previousTransaction in previousTransactions) {

                val transaction = Transaction(
                    walletService.getWalletOrThrowException(previousTransaction.toWallet.getId()!!),
                    walletFrom,
                    TransactionType.ORDER_REFUND,
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