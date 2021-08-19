package it.polito.wa2.ecommerce.catalogueservice.dto

import it.polito.wa2.ecommerce.catalogueservice.domain.Category
import it.polito.wa2.ecommerce.catalogueservice.domain.Comment
import it.polito.wa2.ecommerce.catalogueservice.domain.Product
import java.math.BigDecimal
import java.util.*

data class ProductDTO(
    val id: String,
    val name: String,
    val description: String,
    val photoUrl: String,
    val category: Category,
    val price: BigDecimal,
    val numStars: Long,
    val numRatings: Long,
    val creationDate: Date,
    val commentsUrl: String
)