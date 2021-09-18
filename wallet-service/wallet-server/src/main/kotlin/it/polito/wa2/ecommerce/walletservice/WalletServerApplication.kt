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

            //Id: "1"
            val w1 = walletRepository.save(
                Wallet("1", WalletType.CUSTOMER, BigDecimal("0.00"))
            ) //TODO update ids

            //Id: "2"
            val w2 = walletRepository.save(
                Wallet("2", WalletType.WAREHOUSE, BigDecimal("50.00"))
            )

            //Id:"3"
            val w3 = walletRepository.save(
                Wallet("2", WalletType.CUSTOMER, BigDecimal("100.00"))
            )


            //Id:"4"
            val t1 = Transaction(null, w3, TransactionType.RECHARGE, BigDecimal("150.00"), UUID.randomUUID().toString())

            //Id:"5"
            val t2 = Transaction(w3, w2, TransactionType.ORDER_PAYMENT, BigDecimal("50.00"), "1") //TODO Set an order id

            transactionRepository.save(t1)
            transactionRepository.save(t2)


        }
    }
}


fun main(args: Array<String>) {
    runApplication<WalletServerApplication>(*args)
}
