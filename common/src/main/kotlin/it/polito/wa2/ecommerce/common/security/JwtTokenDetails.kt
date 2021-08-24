package it.polito.wa2.ecommerce.common.security

import it.polito.wa2.ecommerce.common.Rolename

data class JwtTokenDetails(
    val id: String,
    private val username: String,
    private val roles: Set<Rolename>
    )
