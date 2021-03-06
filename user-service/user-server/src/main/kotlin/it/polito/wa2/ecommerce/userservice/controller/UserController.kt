package it.polito.wa2.ecommerce.userservice.controller

import it.polito.wa2.ecommerce.common.security.utils.JwtUtils
import it.polito.wa2.ecommerce.common.Rolename
import it.polito.wa2.ecommerce.common.exceptions.BadRequestException
import it.polito.wa2.ecommerce.common.parseID
import it.polito.wa2.ecommerce.userservice.client.AddRolesRequestDTO
import it.polito.wa2.ecommerce.userservice.client.PasswordChangeRequestDTO
import it.polito.wa2.ecommerce.common.security.UserDetailsDTO
import it.polito.wa2.ecommerce.userservice.client.UserUpdateRequestDTO
import it.polito.wa2.ecommerce.userservice.service.impl.UserDetailsServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.constraints.NotNull

@RestController
@RequestMapping("/users")
@Validated
class UserController {

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

    @PatchMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    fun updateUserFields(
        @PathVariable("userId") userId: String,
        @RequestBody @NotNull updateRequest: UserUpdateRequestDTO
    ): UserDetailsDTO{
        return userDetailsService.updateUserFields(userId.parseID(), updateRequest).copy(password = null)
    }

    @PutMapping("/{userId}/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun changePassword(
        @PathVariable userId: String,
        @RequestBody @Valid request: PasswordChangeRequestDTO, bindingResult: BindingResult
        ){
        if (bindingResult.hasErrors()) {
            throw BadRequestException(bindingResult.fieldErrors.joinToString())
        }

        if(request.newPassword != request.confirmNewPassword){
            throw BadRequestException("newPassword and confirmNewPassword should be equal")
        }

        return userDetailsService.setPassword(
            userId.parseID(),
            request.oldPassword,
            request.newPassword
        )
    }

    @PatchMapping("/{userId}/roles")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun addRoles(@PathVariable userId: String,
                 @RequestBody @Valid request: AddRolesRequestDTO, bindingResult: BindingResult,){
        if (bindingResult.hasErrors()) {
            throw BadRequestException(bindingResult.fieldErrors.joinToString())
        }

        return userDetailsService.upgradeToAdmin(userId.parseID(), request.roles)
    }
}