package it.polito.wa2.ecommerce.userservice.client

import javax.validation.constraints.NotNull

data class UserUpdateRequestDTO(
    @field:NotNull
    val name: String? = null,
    @field:NotNull
    val surname: String? = null,
    @field:NotNull
    val deliveryAddress: String? = null,
)
