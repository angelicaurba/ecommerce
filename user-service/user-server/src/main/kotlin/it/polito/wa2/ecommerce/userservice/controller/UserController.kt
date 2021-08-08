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

    @Autowired
    lateinit var userDetailsService: UserDetailsServiceImpl

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    // TODO: check if having a string as a path variable can be a problem when routing
    fun getUser(@PathVariable userId: String): UserDetailsDTO{
        return userDetailsService.loadUserById(userId.toLong()).copy(password = null)
    }

    @PutMapping("/{userId}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    // TODO: check if having a string as a path variable can be a problem when routing
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

        // TODO check if oldPassword == password in DB

        if(!userDetailsService.verifyPassword(userId.toLong(), request.oldPassword)){
            // TODO use common exception handlers
            // TODO choose type of exception (for security reasons)
            throw Exception()
        }

        // TODO check if userId can update (maybe if it's the same as the JWT token user, or admin?
        //  But probably an admin can't change the password of a user)
        return userDetailsService.setPassword(userId.toLong(), request.newPassword)
    }

    @PatchMapping("/{userId}/roles")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    // TODO: check if having a string as a path variable can be a problem when routing
    fun changeRoles(@PathVariable userId: String){

        // TODO check if the requester is admin via JWT token
        return userDetailsService.upgradeToAdmin(userId.toLong())
    }
}