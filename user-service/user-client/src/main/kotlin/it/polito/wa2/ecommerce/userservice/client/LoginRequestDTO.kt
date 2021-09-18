package it.polito.wa2.ecommerce.userservice.client

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class LoginRequestDTO (
    @field:Size(max = 30, message = "username should have a maximum of 30 characters")
    @field:NotNull(message = "username must be present")
    val username: String,

    @field:Size(min = 8, message = "password should have a minimum of 8 characters")
    @field:NotNull(message = "password must be present")
    val password: String)
