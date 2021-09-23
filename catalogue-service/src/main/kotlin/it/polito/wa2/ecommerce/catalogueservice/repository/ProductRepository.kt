package it.polito.wa2.ecommerce.catalogueservice.repository

import it.polito.wa2.ecommerce.catalogueservice.domain.Category
import it.polito.wa2.ecommerce.catalogueservice.domain.Product
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux

@Repository
interface ProductRepository : ReactiveMongoRepository<Product, String>{

    @Transactional(readOnly = true)
    fun findByCategory(category: Category, page: Pageable): Flux<Product>

    @Transactional(readOnly = true)
    fun findByIdNotNull(page: Pageable): Flux<Product>
}