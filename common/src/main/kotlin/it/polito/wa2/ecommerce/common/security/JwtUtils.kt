package it.polito.wa2.ecommerce.common.security

import io.jsonwebtoken.*
import it.polito.wa2.ecommerce.common.Rolename
import it.polito.wa2.ecommerce.common.constants.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.security.*
import java.security.spec.InvalidKeySpecException
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*

@Component
class JwtUtils {

    val jwtExpirationMs = JWT_EXPIRATION_MS
    val jwtHeaderName = JWT_HEADER_NAME
    val jwtHeaderStart = JWT_HEADER_START
    val jwtHeaderSeparatorChar = JWT_HEADER_SEPARATOR_CHAR
    val encryptAlgorithm = ENCRYPT_ALGORITHM


    private val publicKey: PublicKey by lazy {
        getPublicKey(
            File("src/main/resources/rsa.pubkey")
        )
    }


    @Throws(IOException::class)
    private fun getKeyFromFile(file: File): ByteArray {
        var fileContent = file.readText()

        // Convert key file to string of Base64 characters:
        // exclusion of "-----BEGIN/END ... KEY-----"
        // exclusion of newlines

        val re = Regex("-----[^-]*-----")
        fileContent = re.replace(fileContent, "")
        fileContent = fileContent.replace("\r", "").replace("\n", "")

        return fileContent.toByteArray()
    }

    @Throws(NoSuchAlgorithmException::class, InvalidKeySpecException::class)
    private fun getPublicKey(file: File): PublicKey {

        var bytes: ByteArray = getKeyFromFile(file)

        bytes = Base64.getDecoder().decode(bytes)
        val spec = X509EncodedKeySpec(bytes)
        val factory = KeyFactory.getInstance(encryptAlgorithm)
        return factory.generatePublic(spec)
    }

    @Throws(NoSuchAlgorithmException::class, InvalidKeySpecException::class)
    private fun getPrivateKey(file: File): PrivateKey {
        var bytes: ByteArray = getKeyFromFile(file)

        bytes = Base64.getDecoder().decode(bytes)
        val spec = PKCS8EncodedKeySpec(bytes)
        val factory = KeyFactory.getInstance(encryptAlgorithm)
        return factory.generatePrivate(spec)
    }


    fun generateJwtToken (authentication: Authentication, privateKeyFile: File): String{
        val userPrincipal: UserDetailsDTO = authentication.principal as UserDetailsDTO
        val privateKey: PrivateKey = getPrivateKey(privateKeyFile)

        return Jwts.builder()
            .setId(userPrincipal.id.toString())
            .setSubject(userPrincipal.username)
            .setIssuedAt(Date())
            .setExpiration(Date(Date().time + jwtExpirationMs))
            .claim("roles", userPrincipal.authorities.map{it.toString()})
            .signWith(SignatureAlgorithm.RS256, privateKey)
            .compact()
    }

    fun validateJwtToken (authToken: String): Boolean {
        try {
            Jwts.parserBuilder().setSigningKey(publicKey).build()
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
        val tokenBody = Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(authToken).body

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