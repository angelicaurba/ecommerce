package it.polito.wa2.ecommerce.catalogueservice.dto

import it.polito.wa2.ecommerce.catalogueservice.domain.Comment
import java.util.*
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

data class AddCommentDTO(
    @field:NotNull
    val title: String,
    @field:NotNull
    val body: String,
    @field:NotNull @field:Min(0) @field:Max(5)
    val stars: Int,
    @field:NotNull
    val productId: String
){
    fun toEntity(): Comment {
        return Comment(
            null,
            title,
            body,
            stars,
            productId
        )
    }
}
