package it.polito.wa2.ecommerce.catalogueservice.service.impl

import it.polito.wa2.ecommerce.catalogueservice.dto.AddCommentDTO
import it.polito.wa2.ecommerce.catalogueservice.dto.CommentDTO
import it.polito.wa2.ecommerce.catalogueservice.dto.ProductDTO
import it.polito.wa2.ecommerce.catalogueservice.repository.CommentRepository
import it.polito.wa2.ecommerce.catalogueservice.repository.ProductRepository
import it.polito.wa2.ecommerce.catalogueservice.service.CommentService
import it.polito.wa2.ecommerce.catalogueservice.service.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CommentServiceImpl: CommentService {
    @Autowired lateinit var commentRepository: CommentRepository
    @Autowired lateinit var productService: ProductService
    @Autowired lateinit var productRepository: ProductRepository

    override fun addComment(productId: String, comment: AddCommentDTO): ProductDTO {
        val product = productService.getProductByIdOrThrowException(productId)
        val addedComment = commentRepository.save(comment.toEntity())
        product.numStars += addedComment.stars
        product.numRatings++
        return productRepository.save(product).toDTO()
    }

    override fun getCommentsByProductId(productId: String): List<CommentDTO> {
        productService.getProductByIdOrThrowException(productId)
        return commentRepository.findByProductId(productId).map { it.toDTO() }
    }
}