package it.polito.wa2.ecommerce.catalogueservice.domain

import it.polito.wa2.ecommerce.catalogueservice.dto.CommentDTO
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.CompoundIndexes
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "comments")
@CompoundIndexes(CompoundIndex(name = "productId_authorUsername", def = "{'productId' : 1, 'authorUsername': 1}", unique = true))
data class Comment(
    @Id val id: String? = null,
    val title: String,
    val body: String,
    val stars: Int,
    val productId: String,
    val authorUsername: String
){
    @CreatedDate
    lateinit var creationDate: Date

    fun toDTO():CommentDTO{
        return CommentDTO(
            title, body, stars, creationDate, productId, authorUsername
        )
    }
}
