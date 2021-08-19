package it.polito.wa2.ecommerce.userservice.controller

import it.polito.wa2.ecommerce.userservice.client.LoginRequest
import it.polito.wa2.ecommerce.userservice.client.UserDetailsDTO
import it.polito.wa2.ecommerce.userservice.security.JwtUtils
import it.polito.wa2.ecommerce.userservice.service.impl.UserDetailsServiceImpl
import it.polito.wa2.ecommerce.userservice.client.RegistrationRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid


@RestController
@RequestMapping("/auth")
@Validated
class AuthController(val userDetailsServiceImpl: UserDetailsServiceImpl) {

    // TODO: To be implemented in Common
//    @Value( "\${application.jwt.jwtHeaderStart}" )
//    lateinit var prefix: String

    @Autowired
    lateinit var jwtUtils: JwtUtils

    @Autowired
    lateinit var authenticationManager: AuthenticationManager

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun registerUser(
        @RequestBody @Valid registrationRequest: RegistrationRequest,
        bindingResult: BindingResult
    ) {
        if (bindingResult.hasErrors()) {
            // TODO use common exception handlers
//            throw BadRequestException(bindingResult.fieldErrors.joinToString())
            throw Exception()
        }

        userDetailsServiceImpl.createUser(registrationRequest)
    }

    @GetMapping("/registrationConfirm")
    @ResponseStatus(HttpStatus.OK)
    fun confirmRegistration(@RequestParam("token") token: String) {

        // call verifytoken
        userDetailsServiceImpl.verifyToken(token)
    }

    @PostMapping("/signin")
    @ResponseStatus(HttpStatus.OK)
    fun signin(
        @RequestBody @Valid loginRequest: LoginRequest, bindingResult: BindingResult,
        response: HttpServletResponse
    ): UserDetails {
        if (bindingResult.hasErrors()) {
            // TODO use common exception handlers
//            throw BadRequestException(bindingResult.fieldErrors.joinToString())
            throw Exception()
        }
        
        val authentication: Authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password)
        )

        // TODO Prefix and "Authorization" in Common
        val prefix = "Bearer"
        response.setHeader("Authorization", "$prefix ${jwtUtils.generateJwtToken(authentication)}")

        return (authentication.principal as UserDetailsDTO).copy(password = null)
    }


}