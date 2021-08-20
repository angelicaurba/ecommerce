package it.polito.wa2.ecommerce.common.security

import it.polito.wa2.ecommerce.common.Rolename
import org.springframework.security.core.userdetails.UserDetails

data class UserDetailsDTO(
    val id: String,
    private val username: String,
    private val roles: Set<Rolename>,
    private val password: String? = null,
    val email: String? = null,
    val name: String? = null,
    val surname: String? = null,
    val deliveryAddress: String? = null,
    val isEnabled: Boolean? = null
): UserDetails {
    override fun getAuthorities(): MutableSet<Rolename> {
        return roles.toMutableSet()
    }

    override fun getPassword(): String {
        return password?:""
    }

    override fun getUsername(): String {
        return username
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return isEnabled?:true
    }
}