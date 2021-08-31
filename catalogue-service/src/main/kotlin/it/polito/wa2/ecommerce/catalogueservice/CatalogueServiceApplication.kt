package it.polito.wa2.ecommerce.catalogueservice

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import io.github.resilience4j.timelimiter.TimeLimiterConfig
import it.polito.wa2.ecommerce.catalogueservice.service.ProductService
import it.polito.wa2.ecommerce.common.ErrorMessageDTO
import it.polito.wa2.ecommerce.common.exceptions.BadRequestException
import it.polito.wa2.ecommerce.orderservice.client.item.ItemDTO
import it.polito.wa2.ecommerce.orderservice.client.order.request.OrderRequestDTO
import it.polito.wa2.ecommerce.orderservice.client.item.PurchaseItemDTO
import org.reactivestreams.Publisher
import org.springframework.beans.factory.annotation.Autowired
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
import java.lang.Exception
import java.time.Duration

@SpringBootApplication(scanBasePackages = ["it.polito.wa2.ecommerce.catalogueservice", "it.polito.wa2.ecommerce.common.security"])
class CatalogueServiceApplication {

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
            throw BadRequestException("Error in checking products' price")
    }

}


fun main(args: Array<String>) {
    runApplication<CatalogueServiceApplication>(*args)
}
