package it.polito.wa2.ecommerce.walletservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WalletServerApplication

fun main(args: Array<String>) {
    runApplication<WalletServerApplication>(*args)
}
