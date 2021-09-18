package it.polito.wa2.ecommerce.catalogueservice.domain

import it.polito.wa2.ecommerce.catalogueservice.dto.CommentDTO
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.CompoundIndexes
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

@Document(collection = "comments")
@CompoundIndexes(CompoundIndex(name = "productId_authorUsername", def = "{'productId' : 1, 'authorUsername': 1}", unique = true))
data class Comment(
    @Id @field:NotNull
    val id: String? = null,
    @field:NotNull
    val title: String,
    @field:NotNull
    val body: String,
    @field:NotNull @field:Min(0) @field:Max(5)
    val stars: Int,
    @field:NotNull
    val productId: String,
    @field:NotNull
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
