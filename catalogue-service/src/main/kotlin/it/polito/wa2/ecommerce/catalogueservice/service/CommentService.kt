package it.polito.wa2.ecommerce.catalogueservice.service

import it.polito.wa2.ecommerce.catalogueservice.dto.AddCommentDTO
import it.polito.wa2.ecommerce.catalogueservice.dto.CommentDTO
import it.polito.wa2.ecommerce.catalogueservice.dto.ProductDTO
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface CommentService {
    fun addComment(productId: String, comment: Mono<AddCommentDTO>): Mono<ProductDTO>
    fun getCommentsByProductId(productId: String): Flux<CommentDTO>
}