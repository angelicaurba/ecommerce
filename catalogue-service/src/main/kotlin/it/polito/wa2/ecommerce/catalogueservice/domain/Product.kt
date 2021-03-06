package it.polito.wa2.ecommerce.catalogueservice.domain

import it.polito.wa2.ecommerce.catalogueservice.dto.ProductDTO
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.Digits
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

@Document(collection = "products")
class Product(
    @Id
    @field:NotNull
    var id: String? = null,
    @field:NotNull
    var name: String,
    @field:NotNull
    var description: String,
    @field:NotNull
    var category: Category,
    price: BigDecimal,
    @field:NotNull
    @field:Min(0)
    var numStars: Long,
    @field:NotNull
    @field:Min(0)
    var numRatings: Long,
    @field:NotNull
    var creationDate: Date = Date()
) {
    @field:NotNull
    @field:DecimalMin("0.00", inclusive = true)
    @field:Digits(fraction = 2, integer = 10)
    var price: BigDecimal = price.setScale(2, RoundingMode.HALF_EVEN)
    set(value) {
        field = value.setScale(2, RoundingMode.HALF_EVEN)
    }


    fun toDTO(): ProductDTO {
        val rating = if(numRatings == 0L) BigDecimal.valueOf(0).setScale(2, RoundingMode.HALF_EVEN) else BigDecimal.valueOf(numStars.toDouble()/numRatings.toDouble()).setScale(2, RoundingMode.HALF_EVEN)
        return ProductDTO(
            id!!,
            name,
            description,
            "/products/$id/picture",
            category,
            price,
            rating,
            creationDate,
            "/products/$id/comments"
        )
    }
}

enum class Category {
    HOME, FOOD, TECH, GAMES, CLOTHES, MUSIC, BOOKS, OTHER
}