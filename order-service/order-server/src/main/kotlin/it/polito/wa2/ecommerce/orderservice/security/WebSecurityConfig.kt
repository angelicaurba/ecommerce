package it.polito.wa2.ecommerce.orderservice.security

import it.polito.wa2.ecommerce.common.security.AuthenticationEntryPointImpl
import it.polito.wa2.ecommerce.common.security.JwtAuthenticationTokenFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
class WebSecurityConfig: WebSecurityConfigurerAdapter() {

    @Autowired
    lateinit var authenticationJwtTokenFilter: JwtAuthenticationTokenFilter

    @Autowired
    lateinit var authenticationEntryPoint: AuthenticationEntryPoint

    @Autowired
    lateinit var accessDeniedHandler: AccessDeniedHandler

   override fun configure(httpSecurity: HttpSecurity){

       http
           .authorizeRequests()
           .anyRequest()
           .authenticated()

       http
           .cors().disable()
           .csrf().disable()
           .httpBasic().disable()
           .formLogin().disable()
           .sessionManagement().disable()
           .exceptionHandling()
           .authenticationEntryPoint(authenticationEntryPoint)
           .accessDeniedHandler(accessDeniedHandler)

       http.addFilterBefore(authenticationJwtTokenFilter,
           UsernamePasswordAuthenticationFilter::class.java)

   }

}