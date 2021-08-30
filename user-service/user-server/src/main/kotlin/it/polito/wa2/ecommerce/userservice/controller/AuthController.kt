package it.polito.wa2.ecommerce.userservice.controller

import it.polito.wa2.ecommerce.common.exceptions.BadRequestException
import it.polito.wa2.ecommerce.userservice.client.LoginRequest
import it.polito.wa2.ecommerce.common.security.UserDetailsDTO
import it.polito.wa2.ecommerce.common.security.JwtUtils
import it.polito.wa2.ecommerce.userservice.service.impl.UserDetailsServiceImpl
import it.polito.wa2.ecommerce.userservice.client.RegistrationRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
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

    @Autowired
    lateinit var jwtUtils: JwtUtils

    @Autowired
    lateinit var authenticationManager: AuthenticationManager

    @Value("classpath:rsa.key")
    lateinit var privateKeyFile: Resource


    @PostMapping("/register")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun registerUser(
        @RequestBody @Valid registrationRequest: RegistrationRequest,
        bindingResult: BindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw BadRequestException(bindingResult.fieldErrors.joinToString())
        }

        userDetailsServiceImpl.createUser(registrationRequest)
    }

    @GetMapping("/registrationConfirm")
    @ResponseStatus(HttpStatus.OK)
    fun confirmRegistration(@RequestParam("token") token: String) {
        // TODO create wallet when the user is created
        userDetailsServiceImpl.verifyToken(token)
    }

    @PostMapping("/signin")
    @ResponseStatus(HttpStatus.OK)
    fun signin(
        @RequestBody @Valid loginRequest: LoginRequest, bindingResult: BindingResult,
        response: HttpServletResponse
    ): UserDetails {
        if (bindingResult.hasErrors()) {
            throw BadRequestException(bindingResult.fieldErrors.joinToString())
        }
        
        val authentication: Authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password)
        )

        val jwtToken = jwtUtils.generateJwtToken(authentication, privateKeyFile.file)

        response.setHeader(
            jwtUtils.jwtHeaderName,
            jwtUtils.getHeaderFromJwtToken(jwtToken)
        )

        return (authentication.principal as UserDetailsDTO).copy(password = null)
    }

}