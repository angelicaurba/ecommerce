package it.polito.wa2.ecommerce.catalogueservice.repository

import it.polito.wa2.ecommerce.catalogueservice.domain.Comment
import it.polito.wa2.ecommerce.catalogueservice.dto.CommentDTO
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux

@Repository
interface CommentRepository : ReactiveMongoRepository<Comment, String>{

    @Transactional(readOnly = true)
    fun findByProductId(productId: String): Flux<Comment>
}