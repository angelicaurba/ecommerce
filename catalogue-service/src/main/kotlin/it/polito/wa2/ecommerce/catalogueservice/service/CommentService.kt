package it.polito.wa2.ecommerce.catalogueservice.service

import it.polito.wa2.ecommerce.catalogueservice.dto.CommentDTO
import it.polito.wa2.ecommerce.catalogueservice.dto.ProductDTO

interface CommentService {
    fun addComment(productId: String, comment: CommentDTO): ProductDTO
    fun getCommentsByProductId(productId: String): List<CommentDTO>
}