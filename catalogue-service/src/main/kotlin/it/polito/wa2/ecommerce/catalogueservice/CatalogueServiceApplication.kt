package it.polito.wa2.ecommerce.catalogueservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CatalogueServiceApplication

fun main(args: Array<String>) {
    runApplication<CatalogueServiceApplication>(*args)
}

// TODO nel gateway fare il proxy della post /orders mappando i prodotti (id e amount) aggiungendo il prezzo
