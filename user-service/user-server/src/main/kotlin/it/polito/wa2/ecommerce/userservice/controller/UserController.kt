package it.polito.wa2.ecommerce.userservice.controller

import it.polito.wa2.ecommerce.common.security.JwtUtils
import it.polito.wa2.ecommerce.common.Rolename
import it.polito.wa2.ecommerce.common.exceptions.BadRequestException
import it.polito.wa2.ecommerce.common.parseID
import it.polito.wa2.ecommerce.userservice.client.AddRolesRequest
import it.polito.wa2.ecommerce.userservice.client.PasswordChangeRequest
import it.polito.wa2.ecommerce.common.security.UserDetailsDTO
import it.polito.wa2.ecommerce.userservice.service.impl.UserDetailsServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/users")
@Validated
class UserController {

    // TODO asymmetric key

    @Autowired
    lateinit var userDetailsService: UserDetailsServiceImpl

    @Autowired
    lateinit var jwtUtils: JwtUtils

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    fun getUser(@PathVariable userId: String): UserDetailsDTO {
        return userDetailsService.loadUserById(userId.parseID()).copy(password = null)
    }

    @GetMapping("/{userId}/email")
    @ResponseStatus(HttpStatus.OK)
    fun getEmail(@PathVariable userId: String): String{
        return userDetailsService.loadUserEmailById(userId.parseID())
    }

    @GetMapping("/{userId}/roles")
    @ResponseStatus(HttpStatus.OK)
    fun getRoles(@PathVariable userId: String): Set<Rolename>{
        return userDetailsService.loadUserRolesById(userId.parseID())
    }

    @PutMapping("/{userId}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun changePassword(
        @PathVariable userId: String,
        @RequestBody @Valid request: PasswordChangeRequest, bindingResult: BindingResult,
        @RequestHeader headers: Map<String, String>
        ){
        if (bindingResult.hasErrors()) {
            throw BadRequestException(bindingResult.fieldErrors.joinToString())
        }

        if(request.newPassword != request.confirmNewPassword){
            throw BadRequestException("newPassword and confirmNewPassword should be equal")
        }

        val jwtCompleteHeader = headers[jwtUtils.jwtHeaderName.lowercase()]!!

        return userDetailsService.setPassword(
            userId.parseID(),
            request.oldPassword,
            request.newPassword,
            jwtUtils.getJwtTokenFromHeader(jwtCompleteHeader)
        )
    }

    @PatchMapping("/{userId}/roles")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun addRoles(@PathVariable userId: String,
        @RequestBody @Valid request: AddRolesRequest, bindingResult: BindingResult,){
        if (bindingResult.hasErrors()) {
            throw BadRequestException(bindingResult.fieldErrors.joinToString())
        }

        return userDetailsService.upgradeToAdmin(userId.parseID(), request.roles)
    }
}