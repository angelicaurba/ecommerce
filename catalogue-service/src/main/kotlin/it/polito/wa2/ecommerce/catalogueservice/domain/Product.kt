package it.polito.wa2.ecommerce.catalogueservice.domain

import it.polito.wa2.ecommerce.catalogueservice.dto.ProductDTO
import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.util.*

@Document(collection = "products")
data class Product(
    @Id val id: String? = null,
    var name: String,
    var description: String,
    var category: Category,
    var price: BigDecimal,
    var numStars: Long,
    var numRatings: Long,
    ){
    fun toDTO(): ProductDTO{
        return ProductDTO(
            id!!,
            name,
            description,
            " /products/$id/picture",
            category,
            price,
            numStars,
            numRatings,
            creationDate,
            "/products/$id/comments"
        )
    }
    @CreatedDate
    lateinit var creationDate: Date
}

enum class Category{
    HOME, FOOD, TECH, OTHER
}