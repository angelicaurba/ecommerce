package it.polito.wa2.ecommerce.common.security

import it.polito.wa2.ecommerce.common.constants.JWT_HEADER_NAME
import it.polito.wa2.ecommerce.common.constants.JWT_HEADER_START
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class JwtAuthenticationTokenFilter: OncePerRequestFilter() {

    val header = JWT_HEADER_NAME
    val prefix = JWT_HEADER_START

    @Autowired lateinit var jwtUtils: JwtUtils

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try{
            val authorizationHeader = request.getHeader(header)

            if(authorizationHeader == null || !authorizationHeader.startsWith(prefix)){
                filterChain.doFilter(request, response)
                return
            }

            val jwtToken = jwtUtils.getJwtTokenFromHeader(authorizationHeader)

            if(!jwtUtils.validateJwtToken(jwtToken)) {
                filterChain.doFilter(request, response)
                return
            }

            val userDetails = jwtUtils.getDetailsFromJwtToken(jwtToken)

            val authentication = UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.roles
            )

            authentication.details = WebAuthenticationDetailsSource()
                                        .buildDetails(request)

            SecurityContextHolder.getContext().authentication = authentication

        }
        catch (e: Throwable){
            // TODO make this exception handler compatible with controllerAdvisor (if it's not already compatible)
            println("GOT EXCEPTION; ${e.javaClass} ${e.message}")
        }

        filterChain.doFilter(request, response)
    }


}