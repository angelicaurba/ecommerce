package it.polito.wa2.ecommerce.orderservice.security

import it.polito.wa2.ecommerce.common.security.JwtAuthenticationTokenFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
class WebSecurityConfig: WebSecurityConfigurerAdapter() {

    @Autowired
    lateinit var authenticationJwtTokenFiletr: JwtAuthenticationTokenFilter

   override fun configure(httpSecurity: HttpSecurity){
       http.formLogin().disable()
       http.cors().disable()
       http.csrf().disable()
       http.sessionManagement().disable()

       http
           .authorizeRequests()
           .anyRequest()
           .authenticated()

       http.addFilterBefore(authenticationJwtTokenFiletr,
           UsernamePasswordAuthenticationFilter::class.java)

   }

}