package it.polito.wa2.ecommerce.catalogueservice.service.impl

import it.polito.wa2.ecommerce.catalogueservice.domain.Category
import it.polito.wa2.ecommerce.catalogueservice.domain.Photo
import it.polito.wa2.ecommerce.catalogueservice.domain.Product
import it.polito.wa2.ecommerce.catalogueservice.dto.CommentDTO
import it.polito.wa2.ecommerce.catalogueservice.dto.ProductDTO
import it.polito.wa2.ecommerce.catalogueservice.dto.ProductRequestDTO
import it.polito.wa2.ecommerce.catalogueservice.repository.CommentRepository
import it.polito.wa2.ecommerce.catalogueservice.repository.PhotoRepository
import it.polito.wa2.ecommerce.catalogueservice.repository.ProductRepository
import it.polito.wa2.ecommerce.catalogueservice.service.ProductService
import it.polito.wa2.ecommerce.common.getPageable
import org.bson.BsonBinarySubType
import org.bson.types.Binary
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.util.*

@Service
@Transactional
class ProductServiceImpl: ProductService {

    @Autowired lateinit var productRepository: ProductRepository

    override fun getProductsByCategory(category: Category, pageIdx: Int, pageSize: Int): List<ProductDTO> {
        val page = getPageable(pageIdx, pageSize)
        return productRepository.findByCategory(category, page).map { it.toDTO() }
    }

    override fun getProducts(pageIdx: Int, pageSize: Int): List<ProductDTO> {
        val page = getPageable(pageIdx, pageSize)
        return productRepository.findAll(page).toList().map { it.toDTO() }
    }

    override fun getProductById(productId: String): ProductDTO {
        return getProductByIdOrThrowException(productId).toDTO()
    }

    override fun addProduct(productRequest: ProductRequestDTO, productId: String?): ProductDTO {
        val newProduct = Product(
            productId,
            productRequest.name!!,
            productRequest.description!!,
            productRequest.category!!,
            productRequest.price!!,
            0,
            0,
            Date()
        )
        return productRepository.insert(newProduct).toDTO()
    }

    override fun updateOrCreateProduct(productId: String, productRequest: ProductRequestDTO): ProductDTO {
        return if(isProductPresent(productId)){
            val product : Product = productRepository.findById(productId).get()
            product.name = productRequest.name!!
            product.description = productRequest.description!!
            product.category = productRequest.category!!
            product.price = productRequest.price!!

            productRepository.insert(product).toDTO()
        } else addProduct(productRequest, productId)
    }

    override fun updateProductFields(productId: String, productRequest: ProductRequestDTO): ProductDTO {
        val product = getProductByIdOrThrowException(productId)

        productRequest.name?.also { product.name = it }
        productRequest.description?.also { product.description = it }
        productRequest.category?.also { product.category = it }
        productRequest.price?.also { product.price = it }

        return productRepository.insert(product).toDTO()
    }

    override fun deleteProduct(productId: String) {
        val product = getProductByIdOrThrowException(productId)
        productRepository.delete(product)
    }

    override fun getProductByIdOrThrowException(productId: String): Product {
        val product = productRepository.findById(productId)
        if (product.isPresent)
            return product.get()
        else
            throw Exception() // TODO put the right exception
    }

    override fun isProductPresent(productId: String): Boolean{
        return productRepository.findById(productId).isPresent
    }

}