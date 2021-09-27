package it.polito.wa2.ecommerce.catalogueservice.service.impl

import it.polito.wa2.ecommerce.catalogueservice.dto.AddCommentDTO
import it.polito.wa2.ecommerce.catalogueservice.dto.CommentDTO
import it.polito.wa2.ecommerce.catalogueservice.dto.ProductDTO
import it.polito.wa2.ecommerce.catalogueservice.repository.CommentRepository
import it.polito.wa2.ecommerce.catalogueservice.repository.ProductRepository
import it.polito.wa2.ecommerce.catalogueservice.service.CommentService
import it.polito.wa2.ecommerce.catalogueservice.service.ProductService
import it.polito.wa2.ecommerce.common.connection.Request
import it.polito.wa2.ecommerce.common.exceptions.BadRequestException
import it.polito.wa2.ecommerce.common.exceptions.ForbiddenException
import it.polito.wa2.ecommerce.common.security.JwtTokenDetails
import it.polito.wa2.ecommerce.common.security.UserDetailsDTO
import it.polito.wa2.ecommerce.warehouseservice.client.WarehouseDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono

@Service
@Transactional
class CommentServiceImpl: CommentService {
    @Autowired lateinit var commentRepository: CommentRepository
    @Autowired lateinit var productService: ProductService
    @Autowired lateinit var productRepository: ProductRepository
    @Autowired lateinit var request: Request

    override fun addComment(productId: String, comment: Mono<AddCommentDTO>): Mono<ProductDTO> {
        val newComment = ReactiveSecurityContextHolder.getContext().map{
            it.authentication.principal  as JwtTokenDetails
        }.zipWith(
            comment
        ).flatMap {
            val c = it.t2
            val p = it.t1
            if (productId != c.productId)
                Mono.error(BadRequestException("Product id of request url and request body should match"))
            else {
                request.doGetReactive("http://order-service/purchases/?productId=$productId&userId=${p.id}", Boolean::class.java, true).toMono()
                    .filter {
                        a -> a
                    }.switchIfEmpty(Mono.error(ForbiddenException("User ${p.id} can not add comment for product $productId")))
                    .map {
                        c.toEntity(p.username)
                    }
            }
        }

        return newComment.zipWith(
            productService.getProductByIdOrThrowException(productId)
        ).flatMap {
            val c = it.t1
            val p = it.t2
            p.numStars += c.stars
            p.numRatings++
            commentRepository.save(c).flatMap {
                productRepository.save(p).map{ x -> x.toDTO() }
                }
            }
        }

    override fun getCommentsByProductId(productId: String): Flux<CommentDTO> {
        return productService.getProductByIdOrThrowException(productId)
            .flatMapMany{
                commentRepository.findByProductId(productId).map { it.toDTO() }
            }
    }
}