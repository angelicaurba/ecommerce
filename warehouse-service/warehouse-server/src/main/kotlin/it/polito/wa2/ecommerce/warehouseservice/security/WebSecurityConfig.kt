package it.polito.wa2.ecommerce.warehouseservice.security

import it.polito.wa2.ecommerce.common.security.JwtAuthenticationTokenFilter

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
class WebSecurityConfig: WebSecurityConfigurerAdapter() {
    // TODO check this class and the routes to be authorized
    @Autowired
    lateinit var authenticationJwtTokenFiletr: JwtAuthenticationTokenFilter

    override fun configure(httpSecurity: HttpSecurity){
        http.formLogin().disable()
        http.cors().disable()
        http.csrf().disable()
        http.sessionManagement().disable() //TODO disable also sessionManagement and form login in other services?

        http.authorizeRequests()
            .antMatchers(HttpMethod.POST,"/warehouses/")
//           .hasIpAddress("172.22.0.0/16")
//           .requestMatchers()
            .permitAll()
            .and()
            .authorizeRequests()
            .anyRequest()
            .authenticated()

        http.addFilterBefore(authenticationJwtTokenFiletr,
            UsernamePasswordAuthenticationFilter::class.java)

    }

}