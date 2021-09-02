package it.polito.wa2.ecommerce.common.security

import it.polito.wa2.ecommerce.common.Rolename

data class JwtTokenDetails(
    val id: String,
    val username: String,
    val roles: Set<Rolename>
    )
