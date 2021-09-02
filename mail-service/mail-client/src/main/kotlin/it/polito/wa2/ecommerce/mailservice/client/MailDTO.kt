package it.polito.wa2.ecommerce.mailservice.client

import it.polito.wa2.ecommerce.common.saga.domain.Emittable
import javax.validation.constraints.NotNull


class MailDTO(
    @field:NotNull(message = "userId must be present")
    val userId: String,
    val userEmail: String?,
    @field:NotNull(message = "subject must be present")
    val subject: String,
    @field:NotNull(message = "mailBody must be present")
    val mailBody: String
): Emittable {
     override fun getId(): String {
         return userId
     }
 }
