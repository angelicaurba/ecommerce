package it.polito.wa2.ecommerce.userservice.controller

import it.polito.wa2.ecommerce.common.exceptions.BadRequestException
import it.polito.wa2.ecommerce.userservice.client.LoginRequestDTO
import it.polito.wa2.ecommerce.common.security.UserDetailsDTO
import it.polito.wa2.ecommerce.common.security.utils.JwtUtils
import it.polito.wa2.ecommerce.userservice.service.impl.UserDetailsServiceImpl
import it.polito.wa2.ecommerce.userservice.client.RegistrationRequestDTO
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.io.File
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


    @PostMapping("/register")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun registerUser(
        @RequestBody @Valid registrationRequestDTO: RegistrationRequestDTO,
        bindingResult: BindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw BadRequestException(bindingResult.fieldErrors.joinToString())
        }

        userDetailsServiceImpl.createUser(registrationRequestDTO)
    }

    @GetMapping("/registrationConfirm")
    @ResponseStatus(HttpStatus.OK)
    fun confirmRegistration(@RequestParam("token") token: String) {
        userDetailsServiceImpl.verifyToken(token)
    }

    @PostMapping("/signin")
    @ResponseStatus(HttpStatus.OK)
    fun signin(
        @RequestBody @Valid loginRequestDTO: LoginRequestDTO, bindingResult: BindingResult,
        response: HttpServletResponse
    ): UserDetails {
        if (bindingResult.hasErrors()) {
            throw BadRequestException(bindingResult.fieldErrors.joinToString())
        }
        
        val authentication: Authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(loginRequestDTO.username, loginRequestDTO.password)
        )

        val privateKeyFile = File("src/main/resources/rsa.key")
        val jwtToken = jwtUtils.generateJwtToken(authentication, privateKeyFile)


        response.setHeader(
            jwtUtils.jwtHeaderName,
            jwtUtils.getHeaderFromJwtToken(jwtToken)
        )

        return (authentication.principal as UserDetailsDTO).copy(password = null)
    }

}