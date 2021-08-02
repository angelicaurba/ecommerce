package it.polito.wa2.ecommerce.userservice.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import it.polito.wa2.ecommerce.userservice.client.UserDetailsDTO
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*

@Component
class JwtUtils {

    @Value("\${application.jwt.jwtSecret}")
    private lateinit var jwtSecret: String

    @Value("\${application.jwt.jwtExpirationMs}")
    var jwtExpirationMs: Long = -1

    private val key: Key by lazy {
        Keys.hmacShaKeyFor(jwtSecret.toByteArray())
    }

    fun generateJwtToken (authentication: Authentication): String{
        val userPrincipal: UserDetailsDTO = authentication.principal as UserDetailsDTO

        return Jwts.builder()
            .setId(userPrincipal.id.toString())
            .setSubject(userPrincipal.username)
            .setIssuedAt(Date())
            .setExpiration(Date(Date().time + jwtExpirationMs))
            .claim("roles", userPrincipal.authorities.map{it.toString()})
            .signWith(key)
            .compact()
    }

}