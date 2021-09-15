package it.polito.wa2.ecommerce.orderservice

import it.polito.wa2.ecommerce.orderservice.client.order.response.Status
import it.polito.wa2.ecommerce.orderservice.domain.Order
import it.polito.wa2.ecommerce.orderservice.domain.PurchaseItem
import it.polito.wa2.ecommerce.orderservice.repository.OrderRepository
import it.polito.wa2.ecommerce.orderservice.repository.PurchaseItemRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import java.math.BigDecimal

@SpringBootApplication(scanBasePackages = ["it.polito.wa2.ecommerce"])
class OrderServerApplication {

    @Bean
    fun populateDB(
        orderRepository: OrderRepository,
        purchaseItemRepository: PurchaseItemRepository
    ): CommandLineRunner {
        return CommandLineRunner {
            //TODO update infos + make coherent with wallet and warehouse
            val o1 = orderRepository.save(
                Order(
                    "1", "Somewhere over the rainbow", "2",
                    Status.ISSUED
                )
            )

            val p2 = PurchaseItem("2", 2, BigDecimal("1500.00"), "5").also { it.order = o1 }
            p2.order = o1
            purchaseItemRepository.save(p2)

            val o3 = orderRepository.save(
                Order(
                    "2", "Somewhere over the rainbow - Nowhere Country", "3",
                    Status.DELIVERED
                )
            )

            val p4 = PurchaseItem("2", 1, BigDecimal("10.00"), "1")
            val p5 = PurchaseItem("1", 1, BigDecimal("1500.00"), "3")

            p4.order = o3
            p5.order = o3
            purchaseItemRepository.save(p4)
            purchaseItemRepository.save(p5)

            val o6 = orderRepository.save(
                Order(
                    "3", "Somewhere over the rainbow - Nowhere Country", "3",
                    Status.CANCELED
                )
            )

            val p7 = PurchaseItem("4", 1, BigDecimal("20.00"), "1")

            p7.order = o6

            purchaseItemRepository.save(p7)


        }
    }
}

fun main(args: Array<String>) {
    runApplication<OrderServerApplication>(*args)
}