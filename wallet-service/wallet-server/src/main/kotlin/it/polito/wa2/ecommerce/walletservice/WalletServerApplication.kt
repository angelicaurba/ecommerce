package it.polito.wa2.ecommerce.walletservice

import it.polito.wa2.ecommerce.walletservice.domain.Transaction
import it.polito.wa2.ecommerce.walletservice.domain.TransactionType
import it.polito.wa2.ecommerce.walletservice.domain.Wallet
import it.polito.wa2.ecommerce.walletservice.domain.WalletType
import it.polito.wa2.ecommerce.walletservice.repository.TransactionRepository
import it.polito.wa2.ecommerce.walletservice.repository.WalletRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.context.annotation.Bean
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import java.math.BigDecimal
import java.util.*

@EnableKafka
@SpringBootApplication(scanBasePackages = [
    "it.polito.wa2.ecommerce.walletservice",
    "it.polito.wa2.ecommerce.common.saga",
    "it.polito.wa2.ecommerce.common.security",
    "it.polito.wa2.ecommerce.common.exceptions"
])
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WalletServerApplication {
    @Bean
    fun populateDB(
        walletRepository: WalletRepository,
        transactionRepository: TransactionRepository
    ): CommandLineRunner {
        return CommandLineRunner {
            transactionRepository.deleteAll()
            walletRepository.deleteAll()

            val w1 = walletRepository.save(
                Wallet("1", WalletType.CUSTOMER, BigDecimal("88.00"))
            )

            val w2 = walletRepository.save(
                Wallet("2", WalletType.CUSTOMER, BigDecimal("0.00"))
            )

            val w3 = walletRepository.save(
                Wallet("3", WalletType.CUSTOMER, BigDecimal("30.00"))
            )

            val w4 = walletRepository.save(
                Wallet("1", WalletType.WAREHOUSE, BigDecimal("1522.00"))
            )

            val w5 = walletRepository.save(
                Wallet("2", WalletType.WAREHOUSE, BigDecimal("3000.00"))
            )

            val w6 = walletRepository.save(
                Wallet("2", WalletType.CUSTOMER, BigDecimal("90.00"))
            )

            val transactions = listOf(
                Transaction(null, w1, TransactionType.RECHARGE, BigDecimal("3100.00"), UUID.randomUUID().toString()),
                Transaction(w1, w5, TransactionType.ORDER_PAYMENT, BigDecimal("3000.00"), "1"),
                Transaction(w1, w4, TransactionType.ORDER_PAYMENT, BigDecimal("12.00"), "1"),
                Transaction(null, w3, TransactionType.RECHARGE, BigDecimal("30.00"), UUID.randomUUID().toString()),
                Transaction(w3, w5, TransactionType.ORDER_PAYMENT, BigDecimal("20.00"), "7"),
                Transaction(w5, w3, TransactionType.ORDER_REFUND, BigDecimal("20.00"), "7"),
                Transaction(null, w6, TransactionType.RECHARGE, BigDecimal("1600.00"), UUID.randomUUID().toString()),
                Transaction(w6, w4, TransactionType.ORDER_PAYMENT, BigDecimal("1510.00"), "4")
            )

            transactionRepository.saveAll(transactions)


        }
    }
}


fun main(args: Array<String>) {
    runApplication<WalletServerApplication>(*args)
}
