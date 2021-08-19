package it.polito.wa2.ecommerce.catalogueservice.service.impl

import it.polito.wa2.ecommerce.catalogueservice.domain.Product
import it.polito.wa2.ecommerce.catalogueservice.repository.ProductRepository

fun getProductByIdOrThrowException(productRepository: ProductRepository, productId: String): Product {
    val product = productRepository.findById(productId)
    if (product.isPresent)
        return product.get()
    else
        throw Exception() // TODO put the right exception
}

fun isProductPresent(productRepository: ProductRepository, productId: String): Boolean{
    return productRepository.findById(productId).isPresent
}