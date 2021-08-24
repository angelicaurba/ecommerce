package it.polito.wa2.ecommerce.userservice.client

import javax.validation.constraints.*

data class RegistrationRequest(
    @field:Size(max = 30, message = "username should have a maximum of 30 characters")
    @field:NotNull(message = "username must be present")
    val username: String,
    @field:Email(message = "email should be valid")
    @field:NotNull(message = "email must be present")
    val email: String,
    @field:NotNull(message = "name must be present")
    val name: String,
    @field:NotNull(message = "surname must be present")
    val surname: String,
    @field:NotNull(message = "deliveryAddress must be present")
    val deliveryAddress: String,
    @field:Size(min = 8, message = "password should have a minimum of 8 characters")
    @field:NotNull(message = "password must be present")
    val password: String,
    @field:Size(min = 8, message = "confirmPassword should have a minimum of 8 characters")
    @field:NotNull(message = "confirmPassword must be present")
    val confirmPassword: String
)