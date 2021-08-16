package it.polito.wa2.ecommerce.catalogueservice.repository

import it.polito.wa2.ecommerce.catalogueservice.domain.Category
import it.polito.wa2.ecommerce.catalogueservice.domain.Product
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface ProductRepository : MongoRepository<Product, String>{

    @Transactional(readOnly = true)
    fun findByCategory(category: Category, page: Pageable): List<Product>
}