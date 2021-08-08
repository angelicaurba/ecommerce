package it.polito.wa2.ecommerce.walletservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["it.polito.wa2.ecommerce"])
class WalletServerApplication

fun main(args: Array<String>) {
    runApplication<WalletServerApplication>(*args)
}
