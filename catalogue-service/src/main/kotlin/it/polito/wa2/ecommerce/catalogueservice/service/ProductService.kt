package it.polito.wa2.ecommerce.catalogueservice.service

import it.polito.wa2.ecommerce.catalogueservice.domain.Category
import it.polito.wa2.ecommerce.catalogueservice.domain.Product
import it.polito.wa2.ecommerce.catalogueservice.dto.CommentDTO
import it.polito.wa2.ecommerce.catalogueservice.dto.ProductDTO
import it.polito.wa2.ecommerce.catalogueservice.dto.ProductRequestDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.multipart.MultipartFile
import reactor.core.publisher.Mono

interface ProductService {

    fun getProductsByCategory(category: Category, pageIdx: Int, pageSize: Int): List<ProductDTO>
    fun getProducts(pageIdx: Int, pageSize: Int): List<ProductDTO>
    fun getProductById(productId: String): ProductDTO
    fun addProduct(productRequest: ProductRequestDTO, productId: String? = null): ProductDTO
    fun updateOrCreateProduct(productId: String, productRequest: ProductRequestDTO): ProductDTO
    fun updateProductFields(productId: String, productRequest: ProductRequestDTO): ProductDTO
    fun deleteProduct(productId: String)
    fun getProductByIdOrThrowException(productId: String): Product
    fun isProductPresent(productId: String): Boolean
    fun getWarehousesContainingProduct(productId: String): Mono<out List<String>>
}