package it.polito.wa2.ecommerce.warehouseservice.service.impl

import it.polito.wa2.ecommerce.common.constants.orderDetailsTopic
import it.polito.wa2.ecommerce.common.constants.orderStatusTopic
import it.polito.wa2.ecommerce.common.constants.paymentTopic
import it.polito.wa2.ecommerce.common.constants.warehouseService
import it.polito.wa2.ecommerce.common.saga.service.MessageService
import it.polito.wa2.ecommerce.common.saga.service.ProcessingLogService
import it.polito.wa2.ecommerce.orderservice.client.order.messages.*
import it.polito.wa2.ecommerce.walletservice.client.order.request.WalletOrderPaymentRequestDTO
import it.polito.wa2.ecommerce.walletservice.client.order.request.WalletOrderRequestDTO
import it.polito.wa2.ecommerce.walletservice.client.transaction.request.OrderTransactionRequestDTO
import it.polito.wa2.ecommerce.warehouseservice.client.order.request.WarehouseOrderRequestCancelDTO
import it.polito.wa2.ecommerce.warehouseservice.client.order.request.WarehouseOrderRequestDTO
import it.polito.wa2.ecommerce.warehouseservice.client.order.request.WarehouseOrderRequestNewDTO
import it.polito.wa2.ecommerce.warehouseservice.service.OrderProcessingService
import it.polito.wa2.ecommerce.warehouseservice.service.StockService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(propagation = Propagation.NESTED)
class OrderProcessingServiceImpl: OrderProcessingService {

    @Autowired
    lateinit var stockService: StockService

    @Autowired
    lateinit var processingLogService: ProcessingLogService

    @Autowired
    lateinit var messageService: MessageService

    override fun process(orderRequestDTO: WarehouseOrderRequestDTO, id: String) {

        val uuid = UUID.fromString(id)
        if(processingLogService.isProcessed(uuid))
            return

        var orderDetails: OrderDetailsDTO? = null
        var paymentRequest: WalletOrderRequestDTO? = null
        var orderStatus: OrderStatus? = null

        try {

            if (orderRequestDTO is WarehouseOrderRequestNewDTO) {

                // List<ProductWarehouseDTO>
                val productsWarehouseDTO = stockService.getWarehouseHavingProducts(orderRequestDTO.productList)

                // if it not throw exception, continue
                // List<OrderTransactionRequestDTO>
                val orderTransactionsRequestDTO = stockService.updateAndRetrieveAmount(orderRequestDTO.productList)

                orderDetails = OrderDetailsDTO(orderRequestDTO.orderId, productsWarehouseDTO)
                paymentRequest = WalletOrderPaymentRequestDTO(
                    orderRequestDTO.buyerWalletId,
                    orderRequestDTO.buyerId,
                    orderRequestDTO.orderId,
                    orderTransactionsRequestDTO
                    )

            } else if (orderRequestDTO is WarehouseOrderRequestCancelDTO) {

                    stockService.cancelRequestUpdate(orderRequestDTO.productList)

            }

        }
        catch (e:Exception){
            orderStatus = OrderStatus(
                orderRequestDTO.orderId,
                ResponseStatus.FAILED,
                e.message)
        }
        finally {
            processingLogService.process(uuid)

            orderStatus?.also {
                messageService.publish(it,
                    EventTypeOrderStatus.OrderItemsNotAvailable.toString(),
                    orderStatusTopic)
            }

            orderDetails?.also {
                messageService.publish(it,
                    "OrderDetails",
                    orderDetailsTopic)
            }

            paymentRequest?.also {
                messageService.publish(it,
                    "PaymentRequest",
                    paymentTopic)
            }

        }
    }
}