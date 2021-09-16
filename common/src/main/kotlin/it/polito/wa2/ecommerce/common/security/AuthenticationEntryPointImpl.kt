package it.polito.wa2.ecommerce.common.security

import it.polito.wa2.ecommerce.common.ErrorMessageDTO
import it.polito.wa2.ecommerce.common.ErrorMessageDTOSerializer
import kotlinx.serialization.json.Json
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class AuthenticationEntryPointImpl : AuthenticationEntryPoint {

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        val writer = response.writer
        val error = ErrorMessageDTO(authException, HttpStatus.UNAUTHORIZED, request.requestURI)
        writer.println(Json.encodeToString( ErrorMessageDTOSerializer, error))
    }
}