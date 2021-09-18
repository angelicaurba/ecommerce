package it.polito.wa2.ecommerce.userservice.client

import it.polito.wa2.ecommerce.common.Rolename
import javax.validation.constraints.NotNull

data class AddRolesRequestDTO(
    @field:NotNull(message = "new roles must be present")
    val roles: Set<Rolename>
)
