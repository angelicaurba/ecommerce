package it.polito.wa2.ecommerce.catalogueservice.domain

import it.polito.wa2.ecommerce.catalogueservice.dto.CommentDTO
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "comments")
data class Comment(
    @Id val id: String? = null,
    val title: String,
    val body: String,
    val stars: Int,
    @Indexed
    val productId: String
){
    @CreatedDate
    lateinit var creationDate: Date

    fun toDTO():CommentDTO{
        return CommentDTO(
            title, body, stars, creationDate, productId
        )
    }
}
