package it.polito.wa2.ecommerce.warehouseservice

import it.polito.wa2.ecommerce.common.CommonApplication
import it.polito.wa2.ecommerce.warehouseservice.domain.Stock
import it.polito.wa2.ecommerce.warehouseservice.domain.Warehouse
import it.polito.wa2.ecommerce.warehouseservice.repository.StockRepository
import it.polito.wa2.ecommerce.warehouseservice.repository.WarehouseRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity

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

    @Autowired
    lateinit var warehouseRepository: WarehouseRepository
    @Autowired
    lateinit var stockRepository: StockRepository

    @Bean
    fun populateDB(): CommandLineRunner {
        return CommandLineRunner {

            stockRepository.deleteAll()
            warehouseRepository.deleteAll()

            // devono esserci esattamente due warehouse (id 1 e 2) e ownerid 3
            // dev'esserci almeno il prodotto 4 nel warehouse 2 con amount >= 1

            val wh1 = warehouseRepository.save(
                Warehouse("wh1","via 1", "3")
            )

            val wh2 = warehouseRepository.save(
                Warehouse("wh2","via 2", "3")
            )

            val stocks = listOf(
                Stock(wh1,"1",10,5),
                Stock(wh1,"2",200,50),
                Stock(wh1,"3",2,10),
                Stock(wh1,"4",0,5),

                Stock(wh2,"1",5,5),
                Stock(wh2,"4",20,10)
            )

            stockRepository.saveAll(stocks)

        }
    }
}

fun main(args: Array<String>) {
    runApplication<WarehouseServerApplication>(*args)
}
