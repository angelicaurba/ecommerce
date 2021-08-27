package it.polito.wa2.ecommerce.walletservice.controller

import it.polito.wa2.ecommerce.common.exceptions.BadRequestException
import it.polito.wa2.ecommerce.common.saga.service.MessageServiceImpl
import it.polito.wa2.ecommerce.walletservice.client.order.request.OrderPaymentRequestDTO
import it.polito.wa2.ecommerce.walletservice.client.order.request.OrderRefundRequestDTO
import it.polito.wa2.ecommerce.walletservice.client.transaction.request.RechargeRequestDTO
import it.polito.wa2.ecommerce.walletservice.client.transaction.TransactionDTO
import it.polito.wa2.ecommerce.walletservice.client.wallet.request.WalletCreationRequestDTO
import it.polito.wa2.ecommerce.walletservice.client.wallet.WalletDTO
import it.polito.wa2.ecommerce.walletservice.service.TransactionService
import it.polito.wa2.ecommerce.walletservice.service.WalletService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.constraints.Min

const val DEFAULT_PAGE_SIZE = "10"
//TODO add validation messages (see previous lab)

@RestController
@RequestMapping("/wallets")
@Validated
class WalletController {

    @Autowired
    lateinit var messageService: MessageServiceImpl

    @Autowired
    lateinit var walletService: WalletService

    @Autowired
    lateinit var transactionService: TransactionService

    private fun errorMapper (bindingResult: BindingResult): String {
        return bindingResult.allErrors.joinToString { it.toString() }
    }

    @PatchMapping("/")
    fun testPublish(@RequestParam("test", defaultValue = "true") test:Boolean){
        if(test)
         messageService.publish(OrderPaymentRequestDTO("123", "123", "1", listOf()),
            "OrderOK", "topic1")
        else
            messageService.publish(OrderRefundRequestDTO("123", "123", "1"),
                "OrderOK", "topic1")
    }

    @GetMapping("/{walletId}")
    fun getWalletById(@PathVariable("walletId") walletId:String): WalletDTO {
        return walletService.getWalletById(walletId)
    }

    @PostMapping("/")
    fun createWallet(@RequestBody @Valid walletCreationRequest: WalletCreationRequestDTO,
                     bindingResult: BindingResult
    ): WalletDTO {
        if(bindingResult.hasErrors())
            throw BadRequestException(errorMapper(bindingResult))
        return walletService.addWallet(walletCreationRequest)
    }

    @DeleteMapping("/{walletId}")
    fun deleteWalletById(@PathVariable("walletId") @Min(1) walletId:String){
        return walletService.deleteWallet(walletId)
    }

    @GetMapping("/{walletId}/transactions")
    fun getTransactionsByTimeInterval(
        @PathVariable("walletId") walletId: String,
        @RequestParam("from") @Min(0) startTime: Long?,
        @RequestParam("to") @Min(0) endTime: Long?,
        @RequestParam("pageIndex", defaultValue = "1") @Min(1) pageIdx: Int,
        @RequestParam("pageSize", defaultValue = DEFAULT_PAGE_SIZE) @Min(
            1
        ) pageSize: Int
    ):List<TransactionDTO>{
        return if (startTime != null && endTime != null)
            transactionService.getTransactionsByWalletIdAndTimeInterval(walletId, startTime, endTime, pageIdx, pageSize)
        else if (startTime == null && endTime == null)
            transactionService.getTransactionsByWalletId(walletId, pageIdx, pageSize)
        else throw BadRequestException("Both parameters (from and to) have to be present, or none")
    }

    @GetMapping("/{walletId}/transactions/{transactionId}")
    fun getTransactionById(
        @PathVariable("walletId") walletId: String,
        @PathVariable("transactionId") transactionId: String
    )
            : TransactionDTO {
        return transactionService.getTransactionByWalletIdAndTransactionId(walletId, transactionId)
    }

    @PostMapping("/{walletId}/recharges")
    fun rechargeWallet(@PathVariable("walletId") walletId: String,
                       @RequestBody @Valid rechargeRequestDTO: RechargeRequestDTO,
                       bindingResult: BindingResult): TransactionDTO {
        if(bindingResult.hasErrors())
            throw BadRequestException(errorMapper(bindingResult))
        return transactionService.rechargeWallet(walletId, rechargeRequestDTO)
    }
}