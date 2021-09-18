package it.polito.wa2.ecommerce.userservice.security

import it.polito.wa2.ecommerce.common.security.JwtAuthenticationTokenFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
class WebSecurityConfig: WebSecurityConfigurerAdapter() {
    @Autowired
    lateinit var passwordEncoder: PasswordEncoder
    @Autowired
    lateinit var userDetailsService: UserDetailsService
    @Autowired
    lateinit var authenticationJwtTokenFilter: JwtAuthenticationTokenFilter
    @Autowired
    lateinit var authenticationEntryPoint: AuthenticationEntryPoint
    @Autowired
    lateinit var accessDeniedHandler: AccessDeniedHandler

    override fun configure (auth: AuthenticationManagerBuilder) {
        auth
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder)
    }

    override fun configure(http: HttpSecurity) {

        http
            .authorizeRequests()
            .antMatchers("/auth/**")
            .permitAll()
            .and()
            .authorizeRequests()
            .antMatchers("/users/**/email")
            .permitAll()
            .and()
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

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager? {
        return super.authenticationManagerBean()
    }

}