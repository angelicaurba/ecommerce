package it.polito.wa2.ecommerce.common.security

import it.polito.wa2.ecommerce.common.Rolename
import it.polito.wa2.ecommerce.common.exceptions.ForbiddenException
import it.polito.wa2.ecommerce.common.parseID
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class IdentityVerifier {
    fun verifyUserIdentityOrIsAdmin(ownerId: Long){
        val principal = SecurityContextHolder.getContext().authentication.principal as JwtTokenDetails
        if(principal.roles.contains(Rolename.ADMIN))
            return

        if (principal.id.parseID()!=ownerId)
            throw ForbiddenException()

    }

    fun verifyIsAdmin(){
        val principal = SecurityContextHolder.getContext().authentication.principal as JwtTokenDetails
        if(principal.roles.contains(Rolename.ADMIN))
            return
    }
}