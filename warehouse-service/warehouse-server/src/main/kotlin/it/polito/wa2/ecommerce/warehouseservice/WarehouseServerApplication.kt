package it.polito.wa2.ecommerce.warehouseservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["it.polito.wa2.ecommerce"])
class WarehouseServerApplication

fun main(args: Array<String>) {
    runApplication<WarehouseServerApplication>(*args)
}
