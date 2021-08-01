package it.polito.wa2.ecommerce.userservice.client

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class PasswordChangeRequest (
    @field:Size(min = 8, message = "password should have a minimum of 8 characters")
    @field:NotNull(message = "password must be present")
    val oldPassword: String,

    @field:Size(min = 8, message = "password should have a minimum of 8 characters")
    @field:NotNull(message = "password must be present")
    val newPassword: String,

    @field:Size(min = 8, message = "password should have a minimum of 8 characters")
    @field:NotNull(message = "password must be present")
    val confirmNewPassword: String)