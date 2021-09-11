package it.polito.wa2.ecommerce.warehouseservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient

@SpringBootApplication(scanBasePackages = ["it.polito.wa2.ecommerce"])
@EnableEurekaClient
class WarehouseServerApplication {

}

fun main(args: Array<String>) {
    runApplication<WarehouseServerApplication>(*args)
}
