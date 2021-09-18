package it.polito.wa2.ecommerce.orderservice

import it.polito.wa2.ecommerce.orderservice.client.order.response.Status
import it.polito.wa2.ecommerce.orderservice.domain.Order
import it.polito.wa2.ecommerce.orderservice.domain.PurchaseItem
import it.polito.wa2.ecommerce.orderservice.repository.OrderRepository
import it.polito.wa2.ecommerce.orderservice.repository.PurchaseItemRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import java.math.BigDecimal

@SpringBootApplication(scanBasePackages = [
    "it.polito.wa2.ecommerce.orderservice",
    "it.polito.wa2.ecommerce.common.saga",
    "it.polito.wa2.ecommerce.common.exceptions",
    "it.polito.wa2.ecommerce.common.security"
])
@EnableGlobalMethodSecurity(prePostEnabled = true)
class OrderServerApplication {

    @Bean
    fun populateDB(
        orderRepository: OrderRepository,
        purchaseItemRepository: PurchaseItemRepository
    ): CommandLineRunner {
        return CommandLineRunner {
          
            val o1 = orderRepository.save(
                Order(
                    "1", "Somewhere over the rainbow", "1",
                    Status.ISSUED
                )
            )

            val p2 = PurchaseItem("2", 2, BigDecimal("1500.00"), "2").also { it.order = o1 }
            val p3 = PurchaseItem("3", 1, BigDecimal("12.00"), "1").also { it.order = o1 }
            purchaseItemRepository.save(p2)
            purchaseItemRepository.save(p3)

            val o4 = orderRepository.save(
                Order(
                    "2", "Somewhere over the rainbow - Nowhere Country", "6",
                    Status.DELIVERED
                )
            )

            val p5 = PurchaseItem("2", 1, BigDecimal("10.00"), "1")
            val p6 = PurchaseItem("1", 1, BigDecimal("1500.00"), "1")

            p5.order = o4
            p6.order = o4
            purchaseItemRepository.save(p5)
            purchaseItemRepository.save(p6)

            val o7 = orderRepository.save(
                Order(
                    "3", "Somewhere over the rainbow - Nowhere Country", "3",
                    Status.CANCELED
                )
            )

            val p8 = PurchaseItem("4", 1, BigDecimal("20.00"), "2")

            p8.order = o7

            purchaseItemRepository.save(p8)


        }
    }
}

fun main(args: Array<String>) {
    runApplication<OrderServerApplication>(*args)
}