package it.polito.wa2.ecommerce.catalogueservice.security

import it.polito.wa2.ecommerce.common.constants.JWT_HEADER_NAME
import it.polito.wa2.ecommerce.common.constants.JWT_HEADER_START
import it.polito.wa2.ecommerce.common.security.utils.JwtUtils
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class JwtAuthenticationManager : ReactiveAuthenticationManager {
    override fun authenticate(authentication: Authentication): Mono<Authentication> {
        return Mono.just(authentication)
    }
}


@Component
class JwtServerAuthenticationConverter(private val jwtUtils: JwtUtils) : ServerAuthenticationConverter {
    override fun convert(exchange: ServerWebExchange?): Mono<Authentication> {
        val result = exchange?.request?.headers?.get(JWT_HEADER_NAME)
            ?.firstOrNull {
                it.startsWith(JWT_HEADER_START)
            }
            ?.let {
                val bearer = jwtUtils.getJwtTokenFromHeader(it)
                if (jwtUtils.validateJwtToken(bearer))
                    jwtUtils.getDetailsFromJwtToken(bearer)
                else
                    null
            }
        return Mono.justOrEmpty(result)
            .map { UsernamePasswordAuthenticationToken(it, null, it.roles) }
    }
}