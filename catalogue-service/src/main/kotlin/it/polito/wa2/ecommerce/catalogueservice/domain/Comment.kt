package it.polito.wa2.ecommerce.catalogueservice.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

data class Comment(
//    @Id val id: String? = null,
    // TODO("mettere una collection separata per i commenti?")
    val title: String,
    val body: String,
    val stars: Int,
    val creationDate: Date
)
