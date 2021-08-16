package it.polito.wa2.ecommerce.catalogueservice.domain

import org.bson.types.Binary
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "photos")
data class Photo(
    @Id
    val id: String? = null,
    val format: String,
    val image: Binary,
    @Indexed
    val productId: String
)
