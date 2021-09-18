package it.polito.wa2.ecommerce.catalogueservice.dto

import java.util.*


data class CommentDTO(
    val title: String,
    val body: String,
    val stars: Int,
    val creationDate: Date,
    val productId: String,
    val authorUsername: String
)
