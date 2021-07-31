package it.polito.wa2.ecommerce.catalogueservice.domain

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.util.*

@Document(collection = "products")
data class Product(
    @Id val id: String? = null,
    val name: String,
    val description: String,
    val photoId: ObjectId,
    val category: Category,
    val price: BigDecimal,
    val numStars: Long,
    val numRatings: Long,
    val creationDate: Date,
    val comments: List<Comment>
    )

enum class Category{
    HOME, FOOD, TECH, OTHER
}