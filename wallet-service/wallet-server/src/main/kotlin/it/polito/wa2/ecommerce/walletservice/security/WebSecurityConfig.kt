package it.polito.wa2.ecommerce.walletservice.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter


@Configuration
@EnableWebSecurity
class WebSecurityConfig: WebSecurityConfigurerAdapter() {

   override fun configure(httpSecurity: HttpSecurity){
       http.formLogin().disable()
       http.cors().disable()
       http.csrf().disable()
       http.sessionManagement().disable()
       http.authorizeRequests().anyRequest().permitAll() //TODO change security level
   }

}