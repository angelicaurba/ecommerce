package it.polito.wa2.ecommerce.userservice.client

import it.polito.wa2.ecommerce.common.Rolename

data class RolesChangeRequest (
    val roles: Set<Rolename>)