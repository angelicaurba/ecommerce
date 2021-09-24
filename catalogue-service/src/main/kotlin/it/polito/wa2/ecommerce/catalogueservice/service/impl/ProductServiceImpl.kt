package it.polito.wa2.ecommerce.catalogueservice.service.impl

import it.polito.wa2.ecommerce.catalogueservice.domain.Category
import it.polito.wa2.ecommerce.catalogueservice.domain.Product
import it.polito.wa2.ecommerce.catalogueservice.dto.ProductDTO
import it.polito.wa2.ecommerce.catalogueservice.dto.ProductRequestDTO
import it.polito.wa2.ecommerce.catalogueservice.exceptions.ProductNotFoundException
import it.polito.wa2.ecommerce.catalogueservice.repository.ProductRepository
import it.polito.wa2.ecommerce.catalogueservice.service.ProductService
import it.polito.wa2.ecommerce.common.connection.Request
import it.polito.wa2.ecommerce.common.exceptions.BadRequestException
import it.polito.wa2.ecommerce.common.exceptions.ForbiddenException
import it.polito.wa2.ecommerce.common.getPageable
import it.polito.wa2.ecommerce.warehouseservice.client.WarehouseDTO
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toFlux
import reactor.kotlin.core.publisher.toMono
import java.math.BigDecimal


@Service
@Transactional
class ProductServiceImpl : ProductService {

    @Autowired
    lateinit var productRepository: ProductRepository

    @Autowired
    lateinit var request: Request

    override fun getProductsByCategory(category: Category, pageIdx: Int, pageSize: Int): Flux<ProductDTO> {
        val page = getPageable(pageIdx, pageSize)
        return productRepository.findByCategory(category, page).map{ it.toDTO() }
    }

    override fun getProducts(pageIdx: Int, pageSize: Int): Flux<ProductDTO> {
        val page = getPageable(pageIdx, pageSize)
        return productRepository.findByIdNotNull(page).map { it.toDTO() }
    }

    override fun getProductById(productId: String): Mono<ProductDTO> {
        return getProductByIdOrThrowException(productId).map { it.toDTO()}
    }

    @PreAuthorize("hasAuthority(T(it.polito.wa2.ecommerce.common.Rolename).ADMIN)")
    override fun addProduct(productRequest: Mono<ProductRequestDTO>, productId: String?): Mono<ProductDTO> {
        val newProduct = productRequest.map {
            Product(
                productId,
                it.name!!,
                it.description!!,
                it.category!!,
                it.price!!,
                0,
                0
            )
        }
        return productRepository.insert(newProduct).map { it.toDTO()}.toMono()
    }

    @PreAuthorize("hasAuthority(T(it.polito.wa2.ecommerce.common.Rolename).ADMIN)")
    override fun updateOrCreateProduct(productId: String, productRequest: Mono<ProductRequestDTO>): Mono<ProductDTO> {

        return productRepository.findById(productId)
            .switchIfEmpty(Mono.just(Product(null, "", "", Category.OTHER, BigDecimal("0.00"), 0,0)))
            .zipWith(productRequest)
            .flatMap {
                val req = it.t2
                val product = it.t1
                if (product.id == null)
                    addProduct(Mono.just(req), productId)
                else{
                    product.name = req.name!!
                    product.description = req.description!!
                    product.category = req.category!!
                    product.price = req.price!!
                    productRepository.save(product).map{a -> a.toDTO()}
                }
            }
    }

    @PreAuthorize("hasAuthority(T(it.polito.wa2.ecommerce.common.Rolename).ADMIN)")
    override fun updateProductFields(productId: String, productRequest: Mono<ProductRequestDTO>): Mono<ProductDTO> {
        return getProductByIdOrThrowException(productId)
            .zipWith(
                productRequest
            ).flatMap {
                val req = it.t2
                val product = it.t1

                if (req.price != null && req.price < BigDecimal("0.00"))
                    Mono.error(BadRequestException("Negative price update not allowed"))
                else{
                    req.name?.also {x -> product.name = x }
                    req.description?.also { x -> product.description = x }
                    req.category?.also { x -> product.category = x }
                    req.price?.also { x -> product.price = x }

                    productRepository.save(product).map { x -> x.toDTO()}
                }
            }
    }

    @PreAuthorize("hasAuthority(T(it.polito.wa2.ecommerce.common.Rolename).ADMIN)")
    override fun deleteProduct(productId: String) : Mono<Void> {
        return getProductByIdOrThrowException(productId).flatMap {
            productRepository.delete(it)
        }
    }

    override fun getProductByIdOrThrowException(productId: String): Mono<Product> {
        return productRepository
            .findById(productId)
            .switchIfEmpty(Mono.error(ProductNotFoundException(productId)))
    }

    override fun getWarehousesContainingProduct(productId: String): Flux<WarehouseDTO> {
        return getProductByIdOrThrowException(productId)
            .flatMapMany {
                request.doGetReactive("http://warehouse-service/warehouses/?productID=$productId", WarehouseDTO::class.java).toFlux()
            }
    }
}