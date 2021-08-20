package it.polito.wa2.ecommerce.common.security

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.security.Keys
import it.polito.wa2.ecommerce.common.Rolename
import it.polito.wa2.ecommerce.common.security.UserDetailsDTO
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*

// TODO application properties don't work inside common
const val JWT_EXPIRATION_MS = 1800000
const val JWT_HEADER_NAME = "Authorization"
const val JWT_HEADER_START = "Bearer"
const val JWT_HEADER_SEPARATOR_CHAR = " "
const val JWT_SECRET = "temporary"
@Component
class JwtUtils {

    // TODO application properties don't work inside common

//    @Value("\${application.jwt.jwtSecret}")
//    private lateinit var jwtSecret: String
//
//    @Value("\${application.jwt.jwtExpirationMs}")
//    var jwtExpirationMs: Long = -1
//
//    @Value("\${application.jwt.jwtHeaderName}")
//    lateinit var jwtHeaderName: String
//
//    @Value("\${application.jwt.jwtHeaderStart}")
//    lateinit var jwtHeaderStart: String

    val jwtExpirationMs = JWT_EXPIRATION_MS
    val jwtHeaderName = JWT_HEADER_NAME
    val jwtHeaderStart = JWT_HEADER_START
    val jwtHeaderSeparatorChar = JWT_HEADER_SEPARATOR_CHAR
    val jwtSecret = JWT_SECRET

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

    fun validateJwtToken (authToken: String): Boolean {
        try {
            Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(authToken)
            return true
        } catch (e: SecurityException) {
            println("Invalid JWT signature: " + e.message)
        } catch (e: MalformedJwtException) {
            println("Invalid JWT token: " + e.message)
        } catch (e: ExpiredJwtException) {
            println("JWT token is expired: " + e.message)
        } catch (e: UnsupportedJwtException) {
            println("JWT token is unsupported: " + e.message)
        } catch (e: IllegalArgumentException) {
            println("JWT claims string is empty: " + e.message)
        } catch (e: Throwable){
            println("Generic exception ${e.javaClass}: " + e.message)
        }
        return false
    }

    fun getDetailsFromJwtToken(authToken: String): JwtTokenDetails {

        val tokenBody = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken).body

        return JwtTokenDetails(
            id = tokenBody.id,
            username = tokenBody.subject,
            roles = (tokenBody["roles"] as List<String>).map { Rolename.valueOf(it) }.toSet()
        )
    }

    fun getHeaderFromJwtToken(jwtToken: String): String{
        return jwtHeaderStart + jwtHeaderSeparatorChar + jwtToken
    }

    fun getJwtTokenFromHeader(header: String): String{
        return header.split(jwtHeaderSeparatorChar)[1]
    }


}