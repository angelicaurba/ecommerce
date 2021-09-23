package it.polito.wa2.ecommerce.catalogueservice.gateway

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import io.github.resilience4j.timelimiter.TimeLimiterConfig
import it.polito.wa2.ecommerce.catalogueservice.service.ProductService
import it.polito.wa2.ecommerce.orderservice.client.item.ItemDTO
import it.polito.wa2.ecommerce.orderservice.client.item.PurchaseItemDTO
import it.polito.wa2.ecommerce.orderservice.client.order.request.OrderRequestDTO
import it.polito.wa2.ecommerce.warehouseservice.client.StockRequestDTO
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder
import org.springframework.cloud.client.circuitbreaker.Customizer
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import java.time.Duration

@Configuration
class GatewayConfig {

    @Bean
    fun defaultCustomizer(): Customizer<ReactiveResilience4JCircuitBreakerFactory> {
        return Customizer { factory ->
            factory.configureDefault { id ->
                Resilience4JConfigBuilder(id)
                    .circuitBreakerConfig(
                        CircuitBreakerConfig.ofDefaults()
                    ).timeLimiterConfig(
                        TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(10)).build()
                    ).build()
            }
        }

    }

    @Bean
    fun routes(builder: RouteLocatorBuilder, addPriceToItem: AddPriceToItem, checkProductId: CheckProductId, productService: ProductService): RouteLocator {
        return builder
            .routes()
            .route("wallets") {
                it.path(true, "/wallets/**")
                    .filters { f ->
                        f.circuitBreaker { c -> c.setFallbackUri("forward:/failure") }
                    }
                    .uri("lb://wallet-service")
            }.route("authentication") {
                it.path(true, "/auth/**")
                    .filters { f ->
                        f.circuitBreaker { c -> c.setFallbackUri("forward:/failure") }
                    }
                    .uri("lb://user-service")
            }.route("users") {
                it.path(true, "/users/**")
                    .filters { f ->
                        f.circuitBreaker { c -> c.setFallbackUri("forward:/failure") }
                    }
                    .uri("lb://user-service")
            }.route("send order") {
                it.path(true, "/orders/**")
                    .and().method(HttpMethod.POST)
                    .filters { f ->
                        f.circuitBreaker { c -> c.setFallbackUri("forward:/failure") }
                        f.modifyRequestBody<OrderRequestDTO<ItemDTO>, OrderRequestDTO<PurchaseItemDTO>> { c ->
                            c.rewriteFunction = addPriceToItem
                            c.inClass = OrderRequestDTO::class.java
                            c.outClass = OrderRequestDTO::class.java
                        }
                    }
                    .uri("lb://order-service")
            }.route("orders") {
                it.path(true, "/orders/**")
                    .filters { f ->
                        f.circuitBreaker { c -> c.setFallbackUri("forward:/failure") }
                    }
                    .uri("lb://order-service")
            }
            .route("check productId for stocks endpoints") {
                it.path(true,
                    "/warehouses/{warehouseID}/products/{productID}")
                    .filters { f ->
                        f.circuitBreaker { c -> c.setFallbackUri("forward:/failure") }
                        f.filter { exchange, chain ->
                            productService.getProductByIdOrThrowException(exchange.request.path.elements().last().value())
                                .flatMap {
                                    chain.filter(exchange)
                                }
                        }
                    }
                    .uri("lb://warehouse-service")
            }
            .route("check productId for addStock") {
                it.path(true,
                    "/warehouses/{warehouseID}/products")
                    .and().method(HttpMethod.POST)
                    .filters { f ->
                        f.circuitBreaker { c -> c.setFallbackUri("forward:/failure") }
                        f.modifyRequestBody<StockRequestDTO, StockRequestDTO> { c ->
                            c.rewriteFunction = checkProductId
                            c.inClass = StockRequestDTO::class.java
                            c.outClass = StockRequestDTO::class.java
                        }
                    }
                    .uri("lb://warehouse-service")
            }
            .route("warehouse") {
                it.path(true, "/warehouses/**")
                    .filters { f ->
                        f.circuitBreaker { c ->
                            c.setFallbackUri("forward:/failure")
                        }
                    }
                    .uri("lb://warehouse-service")
            }
            .build()
    }
}