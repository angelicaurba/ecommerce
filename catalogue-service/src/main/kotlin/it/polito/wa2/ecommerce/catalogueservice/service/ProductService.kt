package it.polito.wa2.ecommerce.catalogueservice.service

import it.polito.wa2.ecommerce.catalogueservice.domain.Category
import it.polito.wa2.ecommerce.catalogueservice.dto.CommentDTO
import it.polito.wa2.ecommerce.catalogueservice.dto.ProductDTO
import it.polito.wa2.ecommerce.catalogueservice.dto.ProductRequestDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.multipart.MultipartFile

interface ProductService {

    fun getProductByCategory(category: Category, pageIdx: Int, pageSize: Int): List<ProductDTO>
    fun getProductById(productId: String): ProductDTO
    fun addProduct(productRequest: ProductRequestDTO, productId: String? = null): ProductDTO
    fun updateOrCreateProduct(productId: String, productRequest: ProductRequestDTO): ProductDTO
    fun updateProductFields(productId: String, productRequest: ProductRequestDTO): ProductDTO
    fun deleteProduct(productId: String)
}