package it.polito.wa2.ecommerce.userservice.controller

import it.polito.wa2.ecommerce.common.Rolename
import it.polito.wa2.ecommerce.common.exceptions.BadRequestException
import it.polito.wa2.ecommerce.common.exceptions.ForbiddenException
import it.polito.wa2.ecommerce.common.parseID
import it.polito.wa2.ecommerce.userservice.client.PasswordChangeRequest
import it.polito.wa2.ecommerce.userservice.client.UserDetailsDTO
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

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    fun getUser(@PathVariable userId: String): UserDetailsDTO{
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
        ){
        if (bindingResult.hasErrors()) {
            throw BadRequestException(bindingResult.fieldErrors.joinToString())
        }

        if(request.newPassword != request.confirmNewPassword){
            throw BadRequestException("newPassword and confirmNewPassword should be equal")
        }

        // TODO put in service
        if(!userDetailsService.verifyPassword(userId.parseID(), request.oldPassword)){
            throw ForbiddenException("User and password provided do not match")
        }

        // TODO check if userId can update (if it's the same as the JWT token user)
        return userDetailsService.setPassword(userId.parseID(), request.newPassword)
    }

    @PatchMapping("/{userId}/roles")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun changeRoles(@PathVariable userId: String){

        // TODO more general (give list of roles)
        return userDetailsService.upgradeToAdmin(userId.parseID())
    }
}