package it.polito.wa2.ecommerce.walletservice.controller

import it.polito.wa2.ecommerce.walletservice.client.RechargeRequestDTO
import it.polito.wa2.ecommerce.walletservice.client.TransactionDTO
import it.polito.wa2.ecommerce.walletservice.client.WalletCreationRequestDTO
import it.polito.wa2.ecommerce.walletservice.client.WalletDTO
import it.polito.wa2.ecommerce.walletservice.service.WalletService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.lang.RuntimeException
import javax.validation.Valid
import javax.validation.constraints.Min

const val DEFAULT_PAGE_SIZE = "10"
//TODO add validation messages (see previous lab)

@RestController
@RequestMapping("/wallets")
@Validated
class WalletController {

    @Autowired
    lateinit var walletService: WalletService

    @GetMapping("/{walletId}")
    fun getWalletById(@PathVariable("walletId") walletId:String): WalletDTO {
        return walletService.getWalletById(walletId)
    }

    @PostMapping("/")
    fun createWallet(@RequestBody @Valid walletCreationRequest:WalletCreationRequestDTO,
                     bindingResult: BindingResult
    ):WalletDTO{
        if(bindingResult.hasErrors())
            throw RuntimeException("validation errors") //TODO change exception type
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
            walletService.getTransactionsByWalletIdAndTimeInterval(walletId, startTime, endTime, pageIdx, pageSize)
        else if (startTime == null && endTime == null)
            walletService.getTransactionsByWalletId(walletId, pageIdx, pageSize)
        else throw RuntimeException("Both parameters (from and to) have to be present, or none") //TODO change exception
    }

    @GetMapping("/{walletId}/transactions/{transactionId}")
    fun getTransactionById(
        @PathVariable("walletId") walletId: String,
        @PathVariable("transactionId") transactionId: String
    )
            : TransactionDTO {
        return walletService.getTransactionByWalletIdAndTransactionId(walletId, transactionId)
    }

    @PostMapping("/{walletId}/recharges")
    fun rechargeWallet(@PathVariable("walletId") walletId: String,
                       @RequestBody @Valid rechargeRequestDTO: RechargeRequestDTO,
                        bindingResult: BindingResult): TransactionDTO {
        if(bindingResult.hasErrors())
            throw Exception() //TODO should be bad request
        return walletService.rechargeWallet(walletId, rechargeRequestDTO)
    }
}