package it.polito.wa2.ecommerce.warehouseservice

import it.polito.wa2.ecommerce.common.CommonApplication
import it.polito.wa2.ecommerce.warehouseservice.repository.StockRepository
import it.polito.wa2.ecommerce.warehouseservice.repository.WarehouseRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import java.math.BigDecimal
import java.util.*

@SpringBootApplication(scanBasePackages = [
    "it.polito.wa2.ecommerce.warehouseservice",
    "it.polito.wa2.ecommerce.common.exceptions",
    "it.polito.wa2.ecommerce.common.saga",
    "it.polito.wa2.ecommerce.common.security"
],
    scanBasePackageClasses = [
        CommonApplication::class
    ])
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WarehouseServerApplication {
    @Bean
    fun populateDB(
        warehouseRepository: WarehouseRepository,
        stockRepository: StockRepository
    ): CommandLineRunner {
        return CommandLineRunner {
            stockRepository.deleteAll()
            warehouseRepository.deleteAll()

            // TODO populate db
            // devono esserci esattamente due warehouse (id 1 e 2) e ownerid 3
            // dev'esserci almeno il prodotto 4 nel warehouse 2 con amount >= 1
        }
    }
}

fun main(args: Array<String>) {
    runApplication<WarehouseServerApplication>(*args)
}
