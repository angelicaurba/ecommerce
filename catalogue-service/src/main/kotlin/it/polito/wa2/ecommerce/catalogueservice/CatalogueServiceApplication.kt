package it.polito.wa2.ecommerce.catalogueservice

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import io.github.resilience4j.timelimiter.TimeLimiterConfig
import it.polito.wa2.ecommerce.catalogueservice.domain.Category
import it.polito.wa2.ecommerce.catalogueservice.domain.Comment
import it.polito.wa2.ecommerce.catalogueservice.domain.Photo
import it.polito.wa2.ecommerce.catalogueservice.domain.Product
import it.polito.wa2.ecommerce.catalogueservice.repository.CommentRepository
import it.polito.wa2.ecommerce.catalogueservice.repository.PhotoRepository
import it.polito.wa2.ecommerce.catalogueservice.repository.ProductRepository
import it.polito.wa2.ecommerce.catalogueservice.service.ProductService
import it.polito.wa2.ecommerce.common.ErrorMessageDTO
import it.polito.wa2.ecommerce.common.exceptions.BadRequestException
import it.polito.wa2.ecommerce.orderservice.client.item.ItemDTO
import it.polito.wa2.ecommerce.orderservice.client.item.PurchaseItemDTO
import it.polito.wa2.ecommerce.orderservice.client.order.request.OrderRequestDTO
import org.bson.types.Binary
import org.reactivestreams.Publisher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder
import org.springframework.cloud.client.circuitbreaker.Customizer
import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.math.BigDecimal
import java.net.URL
import java.time.Duration
import java.util.*


@SpringBootApplication(scanBasePackages = ["it.polito.wa2.ecommerce.catalogueservice", "it.polito.wa2.ecommerce.common.security"])
class CatalogueServiceApplication {

    @Bean
    fun populateDB(productRepository: ProductRepository, commentRepository: CommentRepository, photoRepository: PhotoRepository)
    : CommandLineRunner {
        return CommandLineRunner {

            commentRepository.deleteAll()
            photoRepository.deleteAll()
            productRepository.deleteAll()

            val now = Date()
            val nowMinusOne = Date(now.time - 1000*60*60)
            val nowMinusTwo = Date(now.time - 1000*60*60*2)
            val nowMinusFive = Date(now.time - 1000*60*60*5)
            val yesterday = Date(now.time - 1000*60*60*24)

            val p1 = Product(
                "1",
                "Thinkpad L15",
                "personal computer from Lenovo",
                Category.TECH,
                BigDecimal("1200.51"),
                4,
                1,
                yesterday
            )

            val p2 = Product(
                "2",
                "Surface Pro 8",
                "2 in 1",
                Category.TECH,
                BigDecimal("2250.99"),
                0,
                0,
                nowMinusOne
            )

            val p3 = Product(
                "3",
                "A Room of One’s Own",
                "A Room of One’s Own - Virgina Woolf's book",
                Category.BOOKS,
                BigDecimal("12.20"),
                0,
                0,
                nowMinusTwo
            )

            val p4 = Product(
                "4",
                "Harry Potter and the Sorcerer's Stone",
                "First book of J. K. Rowling's saga",
                Category.BOOKS,
                BigDecimal("24.20"),
                9,
                2,
                nowMinusFive
            )

            productRepository.save(p1)
            productRepository.save(p2)
            productRepository.save(p3)
            productRepository.save(p4)

            val c1 = Comment(
                "1",
                "Amazing book!",
                "A great read at an affordable price. I highly recommend it",
                5,
                "4"
            )

            val c2 = Comment(
                "2",
                "Not so bad",
                "It is not a bad read but frankly I prefer the movie",
                3,
                "4"
            )

            val c3 = Comment(
                "3",
                "Great computer",
                "I use it to work in the office: good value for money and good performances. Well done!",
                5,
                "1"
            )

            commentRepository.save(c1)
            commentRepository.save(c2)
            commentRepository.save(c3)

            val imageUrl = "https://i.pinimg.com/736x/1b/2b/fa/1b2bfa193ec0b2e72af49991ea0aff1a.jpg"
            val url = URL(imageUrl)
            val stream: InputStream = url.openStream()
            var byteArray = ByteArrayOutputStream()
            val b = ByteArray(2048)
            var length: Int
            while (stream.read(b).also { length = it } != -1) {
                byteArray.write(b, 0, length)
            }
            stream.close()

            val image  = Binary(byteArray.toByteArray())

            val photo1 = Photo(
                "1",
                "jpg",
                image,
                "4"
            )

            photoRepository.save(photo1)

        }
    }

    @Bean
    fun defaultCustomizer(): Customizer<ReactiveResilience4JCircuitBreakerFactory> {
        return Customizer { factory ->
            factory.configureDefault { id ->
                Resilience4JConfigBuilder(id)
                    .circuitBreakerConfig(
                        CircuitBreakerConfig.ofDefaults()
                    ).timeLimiterConfig(
                        TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(4)).build()
                    ).build()
            }
        }

    }

    @Bean
    fun routes(builder: RouteLocatorBuilder, addPriceToItem: AddPriceToItem): RouteLocator {
        return builder
            .routes()
            .route("warehouse") {
                it.path(true, "/warehouses")
                    .filters { f ->
                        f.circuitBreaker { c -> c.setFallbackUri("forward:/failure") }
                    }
                    .uri("lb://warehouses")
            }.route("wallets") {
                it.path(true, "/wallets")
                    .filters { f ->
                        f.circuitBreaker { c -> c.setFallbackUri("forward:/failure") }
                    }
                    .uri("lb://wallets")
            }.route("send order") {
                it.path(true, "/orders")
                    .and().method(HttpMethod.POST)
                    .filters { f ->
                        //TODO fix circuit breaker
//                        f.circuitBreaker { c -> c.setFallbackUri("forward:/failure") }
                        f.modifyRequestBody<OrderRequestDTO<ItemDTO>, OrderRequestDTO<PurchaseItemDTO>> { c ->
                            c.rewriteFunction = addPriceToItem
                            c.inClass = OrderRequestDTO::class.java
                            c.outClass = OrderRequestDTO::class.java
                        }
                    }
                    .uri("lb://orders")
            }.route("orders") {
                it.path(true, "/orders")
                    .filters { f ->
                        f.circuitBreaker { c -> c.setFallbackUri("forward:/failure") }
                    }
                    .uri("lb://orders")
            }
            .build()
    }

}

@RestController
class FailureController{

    @GetMapping("/failure")
    fun reactToFailureGET(): ErrorMessageDTO{
        return ErrorMessageDTO(Exception("Unable to connect"), HttpStatus.GATEWAY_TIMEOUT, "")
    }

    @PostMapping("/failure")
    fun reactToFailurePOST(): ErrorMessageDTO{
        return ErrorMessageDTO(Exception("Unable to connect"), HttpStatus.BAD_GATEWAY, "")
    }
}

@Component
class AddPriceToItem : RewriteFunction<OrderRequestDTO<ItemDTO>, OrderRequestDTO<PurchaseItemDTO>> {

    @Autowired
    lateinit var productService: ProductService

    override fun apply(t: ServerWebExchange?, u: OrderRequestDTO<ItemDTO>?): Publisher<OrderRequestDTO<PurchaseItemDTO>> {
        var newOrderRequest: OrderRequestDTO<PurchaseItemDTO>? = null
        if (u != null) {
            newOrderRequest = OrderRequestDTO<PurchaseItemDTO>(
                u.buyerId,
                u.buyerWalletId,
                u.address,
                u.deliveryItems
                    .map { item ->
                        PurchaseItemDTO(
                            item.productId,
                            item.amount,
                            productService.getProductById(item.productId).price
                        )
                    }
            )
        }

        if (newOrderRequest != null)
            return Mono.just(newOrderRequest)
        else
            throw BadRequestException("Error in checking products' prices")
    }

}


fun main(args: Array<String>) {
    runApplication<CatalogueServiceApplication>(*args)
}
