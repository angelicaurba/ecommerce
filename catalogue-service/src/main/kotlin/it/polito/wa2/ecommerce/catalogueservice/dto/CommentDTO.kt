package it.polito.wa2.ecommerce.catalogueservice.dto

import it.polito.wa2.ecommerce.catalogueservice.domain.Comment
import java.util.*
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull


data class CommentDTO(
    val title: String,
    val body: String,
    val stars: Int,
    val creationDate: Date,
    val productId: String
)
