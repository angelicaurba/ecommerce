package it.polito.wa2.ecommerce.catalogueservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean

@SpringBootApplication
class CatalogueServiceApplication{

    @Bean
    fun routes(builder: RouteLocatorBuilder): RouteLocator{
        return builder
            .routes()
            .build()
    }
}

fun main(args: Array<String>) {
    runApplication<CatalogueServiceApplication>(*args)
}

// TODO nel gateway fare il proxy della post /orders mappando i prodotti (id e amount) aggiungendo il prezzo
