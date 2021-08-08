package it.polito.wa2.ecommerce.common

import org.springframework.security.core.GrantedAuthority

enum class Rolename: GrantedAuthority {
    CUSTOMER,
    ADMIN;

    override fun getAuthority(): String {
        return this.name
    }
}