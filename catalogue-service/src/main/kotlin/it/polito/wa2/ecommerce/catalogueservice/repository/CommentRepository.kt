package it.polito.wa2.ecommerce.catalogueservice.repository

import it.polito.wa2.ecommerce.catalogueservice.domain.Comment
import it.polito.wa2.ecommerce.catalogueservice.dto.CommentDTO
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.transaction.annotation.Transactional

interface CommentRepository : MongoRepository<Comment, String>{

    @Transactional(readOnly = true)
    fun findByProductId(productId: String): List<Comment>
}