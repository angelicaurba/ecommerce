package it.polito.wa2.ecommerce.walletservice.controller

import it.polito.wa2.ecommerce.walletservice.client.RechargeRequestDTO
import it.polito.wa2.ecommerce.walletservice.client.TransactionDTO
import it.polito.wa2.ecommerce.walletservice.client.WalletCreationRequestDTO
import it.polito.wa2.ecommerce.walletservice.client.WalletDTO
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.constraints.Min

const val DEFAULT_PAGE_SIZE = "10"
//TODO add validation messages (see previous lab)

@RestController
@RequestMapping("/wallets")
@Validated
class WalletController {

    @GetMapping("/{walletId}")
    fun getWalletById(@PathVariable("walletId") @Min(1) walletId:Int): WalletDTO {
        TODO("implement me")
    }

    @PostMapping("/")
    fun createWallet(@RequestBody walletCreationRequest:WalletCreationRequestDTO ):WalletDTO{
        TODO("implement me")
    }

    @DeleteMapping("/{walletId}")
    fun deleteWalletById(@PathVariable("walletId") @Min(1) walletId:Int){
        TODO("implement me")
    }

    @GetMapping("/{walletId}/transactions")
    fun getTransactionsByTimeInterval(
        @PathVariable("walletId") @Min(1) walletId: Long,
        @RequestParam("from") @Min(0) startTime: Long?,
        @RequestParam("to") @Min(0) endTime: Long?,
        @RequestParam("pageIndex", defaultValue = "1") @Min(1) pageIdx: Int,
        @RequestParam("pageSize", defaultValue = DEFAULT_PAGE_SIZE) @Min(
            1
        ) pageSize: Int
    ):List<TransactionDTO>{
        TODO("Implement me")
    }

    @GetMapping("/{walletId}/transactions/{transactionId}")
    fun getTransactionById(
        @PathVariable("walletId") @Min(1) walletId: Long,
        @PathVariable("transactionId") @Min(1) transactionId: Long
    )
            : TransactionDTO {
        TODO("implement me")
    }

    @PostMapping("/{walletId}/recharges")
    fun rechargeWallet(@PathVariable("walletId") @Min(1) walletId: Int,
                       @RequestBody rechargeRequestDTO: RechargeRequestDTO): TransactionDTO {
        TODO("Implement me")
    }
}