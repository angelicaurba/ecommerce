package it.polito.wa2.ecommerce.mailservice.client

data class MailDTO(
    val userId: String,
    val userEmail: String?,
    val subject: String,
    val mailBody: String
)
