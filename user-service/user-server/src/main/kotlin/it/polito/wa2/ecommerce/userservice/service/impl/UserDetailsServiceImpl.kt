package it.polito.wa2.ecommerce.userservice.service.impl

import it.polito.wa2.ecommerce.common.Rolename
import it.polito.wa2.ecommerce.userservice.client.UserDetailsDTO
import it.polito.wa2.ecommerce.userservice.domain.User
import it.polito.wa2.ecommerce.userservice.repository.UserRepository
import it.polito.wa2.ecommerce.userservice.service.NotificationService
import it.polito.wa2.group6.dto.RegistrationRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class UserDetailsServiceImpl: UserDetailsService {

    // TODO Connect to mail service
//    @Autowired
//    lateinit var mailService: MailService
    @Autowired
    lateinit var notificationService: NotificationService

    @Autowired
    lateinit var userRepository: UserRepository
    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    fun createUser(registrationRequest: RegistrationRequest){
        if (userRepository.findByUsername(registrationRequest.username) != null) {
            // TODO use common exception handlers
//            throw UsernameAlreadyExists("Username already in use")
            throw Exception()
        }
        if(userRepository.findByEmail(registrationRequest.email) != null) {
            // TODO use common exception handlers
//            throw EmailAlreadyExists("Email already in use")
            throw Exception()
        }
        if (registrationRequest.password != registrationRequest.confirmPassword) {
            // TODO use common exception handlers
//            throw BadRequestException("Passwords do not match")
            throw Exception()
        }

        val user = User(
            registrationRequest.username,
            passwordEncoder.encode(registrationRequest.password),
            registrationRequest.email,
            // TODO should roles be defined in the request object, or with different APIs?
            Rolename.CUSTOMER.toString(),
            registrationRequest.name,
            registrationRequest.surname,
            registrationRequest.deliveryAddress,
            false
        )

        val savedUser = userRepository.save(user)

        val token = notificationService.createEmailVerificationToken(savedUser)

        // TODO Send email to Mail service
        println(token.token)
/*
        mailService.sendMessage(savedUser.email, "Email verification",
            "Hi ${savedUser.name} ${savedUser.surname},\n" +
                    "thank you for signing in to LARA-FoodDelivery! " +
                    "please verify your account by clicking on the link " +
                    "http://localhost:8080/auth/registrationConfirm?token=${token.token} \n" +
                    "Pay attention, this link will remain active up to 30 minutes.")
*/
    }

    fun addRoleByUsername(newRole: Rolename, username: String){
        val user = findUserByUsername(username)
        user.addRole(newRole)
        userRepository.save(user)
    }

    fun removeRoleByUsername(oldRole: Rolename, username: String){
        val user = findUserByUsername(username)
        user.removeRole(oldRole)
        userRepository.save(user)
    }

    override fun loadUserByUsername(username: String): UserDetailsDTO {
        val user = findUserByUsername(username)
        return user.toDTO()
    }

    fun setDeliveryAddress(userId: Long, deliveryAddress: String) {
        val user = userRepository.findByIdOrNull(userId) ?: throw UsernameNotFoundException("User ID not found")
        user.deliveryAddress = deliveryAddress
        userRepository.save(user)
    }

    // TODO: Authorization only in gateway (?)
    // @PreAuthorize("hasAuthority(T(it.polito.wa2.ecommerce.common.Rolename).ADMIN)")
    fun setIsEnabledByUsername(newIsEnabled: Boolean, username: String){
        val user = findUserByUsername(username)
        user.isEnabled = newIsEnabled
        userRepository.save(user)
    }

    private fun findUserByUsername(username: String): User {
        return userRepository.findByUsername(username) ?: throw UsernameNotFoundException("Username not found")
    }

    fun verifyToken(token: String) {
        val verificationToken = notificationService.verifyToken(token)

        val username = verificationToken.user!!.username

        setIsEnabledByUsername(true, username)
        addRoleByUsername(Rolename.CUSTOMER, username)

        notificationService.deleteToken(verificationToken)
    }
}