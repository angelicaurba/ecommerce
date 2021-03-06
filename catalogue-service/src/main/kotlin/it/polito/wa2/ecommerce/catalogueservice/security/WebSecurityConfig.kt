package it.polito.wa2.ecommerce.catalogueservice.security

import it.polito.wa2.ecommerce.common.ErrorMessageDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.ResolvableType
import org.springframework.core.codec.Hints
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity.AuthorizeExchangeSpec
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Configuration
class WebfluxSecurityConfig {

    @Bean
    fun springSecurityFilterChain(http: ServerHttpSecurity,
                                  jwtAuthenticationManager: ReactiveAuthenticationManager,
                                  jwtAuthenticationConverter: ServerAuthenticationConverter
    ): SecurityWebFilterChain {

        val authenticationWebFilter = AuthenticationWebFilter(jwtAuthenticationManager)
        authenticationWebFilter.setServerAuthenticationConverter(jwtAuthenticationConverter)

        http
                // catalogue
            .authorizeExchange()
            .pathMatchers(HttpMethod.GET, "/products/**")
            .permitAll()
            .and()
            .authorizeExchange()
            .pathMatchers("/products/**")
            .authenticated()
                // order
            .and()
            .authorizeExchange()
            .pathMatchers("/orders/**")
            .authenticated()
                // email
            .and()
            .authorizeExchange()
            .pathMatchers("/users/*/email")
            .denyAll()
                // user
            .and()
            .authorizeExchange()
            .pathMatchers("/auth/**")
            .permitAll()
            .and()
            .authorizeExchange()
            .pathMatchers("/users/**")
            .authenticated()
                // wallet
            .and()
            .authorizeExchange()
            .pathMatchers("/wallets/**")
            .authenticated()
                // warehouse
            .and()
            .authorizeExchange()
            .pathMatchers(HttpMethod.GET,"/warehouses/**")
            .permitAll()
            .and()
            .authorizeExchange()
            .pathMatchers("/warehouses/**")
            .authenticated()


        http
            .cors().disable()
            .csrf().disable()
            .httpBasic().disable()
            .formLogin().disable()
            .exceptionHandling()
            .authenticationEntryPoint(handler)
            .accessDeniedHandler(handler)

        http
            .addFilterAt(
                authenticationWebFilter,
                SecurityWebFiltersOrder.AUTHENTICATION)

        return http.build()
    }
}

private val handler = {
    swe: ServerWebExchange, e : Exception ->
        println(e)
        println(e.message)
        val body = ErrorMessageDTO(e, HttpStatus.UNAUTHORIZED, swe.request.path.toString())
        swe.response.statusCode = HttpStatus.UNAUTHORIZED
        swe.response.headers.contentType = MediaType.APPLICATION_JSON
        swe.response.writeWith(
            Jackson2JsonEncoder().encode(
                Mono.just(body),
                swe.response.bufferFactory(),
                ResolvableType.forInstance(body),
                MediaType.APPLICATION_JSON,
                Hints.from(Hints.LOG_PREFIX_HINT, swe.logPrefix)
            )
        )
}