package it.polito.wa2.ecommerce.catalogueservice.exceptions

import it.polito.wa2.ecommerce.common.exceptions.NotFoundException

class ProductNotFoundException(productId: String) : NotFoundException("Product not found with id $productId")
