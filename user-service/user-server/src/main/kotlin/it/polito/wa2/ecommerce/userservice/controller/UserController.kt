package it.polito.wa2.ecommerce.userservice.controller

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
        return userDetailsService.loadUserById(userId.toLong()).copy(password = null)
    }

    // TODO Route to get email

    // TODO Route to get roles

    @PutMapping("/{userId}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun changePassword(
        @PathVariable userId: String,
        @RequestBody @Valid request: PasswordChangeRequest, bindingResult: BindingResult,
        ){
        if (bindingResult.hasErrors()) {
            // TODO use common exception handlers
//            throw BadRequestException(bindingResult.fieldErrors.joinToString())
            throw Exception()
        }

        if(request.newPassword != request.confirmNewPassword){
            // TODO use common exception handlers
//            throw BadRequestException("newPassword and confirmNewPassword should be equal")
            throw Exception()
        }

        // TODO import extension function to parse userId to long with exception

        // TODO put in service
        if(!userDetailsService.verifyPassword(userId.toLong(), request.oldPassword)){
            // TODO use common exception handlers
            // TODO 401 Unauthorized
            throw Exception()
        }

        // TODO check if userId can update (if it's the same as the JWT token user)
        return userDetailsService.setPassword(userId.toLong(), request.newPassword)
    }

    @PatchMapping("/{userId}/roles")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun changeRoles(@PathVariable userId: String){

        // TODO check if the requester is admin via JWT token
        // TODO more general (give list of roles)
        return userDetailsService.upgradeToAdmin(userId.toLong())
    }
}