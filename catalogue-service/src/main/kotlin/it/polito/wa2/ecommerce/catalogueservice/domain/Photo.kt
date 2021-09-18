package it.polito.wa2.ecommerce.catalogueservice.domain

import org.bson.types.Binary
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import javax.validation.constraints.NotNull

@Document(collection = "photos")
data class Photo(
    @Id @field:NotNull
    val id: String? = null,
    @field:NotNull
    val format: String,
    @field:NotNull
    val image: Binary,
    @Indexed(unique=true) @field:NotNull
    val productId: String
)
