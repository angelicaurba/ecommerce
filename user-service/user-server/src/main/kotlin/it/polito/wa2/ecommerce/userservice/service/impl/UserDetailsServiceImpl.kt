package it.polito.wa2.ecommerce.userservice.service.impl

import it.polito.wa2.ecommerce.common.security.JwtUtils
import it.polito.wa2.ecommerce.common.Rolename
import it.polito.wa2.ecommerce.common.constants.mailTopic
import it.polito.wa2.ecommerce.common.exceptions.BadRequestException
import it.polito.wa2.ecommerce.common.exceptions.ForbiddenException
import it.polito.wa2.ecommerce.common.parseID
import it.polito.wa2.ecommerce.common.saga.service.MessageService
import it.polito.wa2.ecommerce.common.security.JwtTokenDetails
import it.polito.wa2.ecommerce.common.security.UserDetailsDTO
import it.polito.wa2.ecommerce.mailservice.client.MailDTO
import it.polito.wa2.ecommerce.userservice.domain.User
import it.polito.wa2.ecommerce.userservice.repository.UserRepository
import it.polito.wa2.ecommerce.userservice.service.NotificationService
import it.polito.wa2.ecommerce.userservice.client.RegistrationRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.annotation.security.RolesAllowed

@Transactional
@Service
class UserDetailsServiceImpl: UserDetailsService {

    @Autowired
    lateinit var messageService: MessageService

    @Autowired
    lateinit var notificationService: NotificationService

    @Autowired
    lateinit var userRepository: UserRepository
    @Autowired
    lateinit var passwordEncoder: PasswordEncoder
    @Autowired
    lateinit var jwtUtils: JwtUtils

    private fun verifyIfAdminOrSameUser(userId: Long){
        val userFromJwtToken = SecurityContextHolder.getContext().authentication.principal as JwtTokenDetails


        if(userFromJwtToken.roles.contains(Rolename.ADMIN) ||
            userId == userFromJwtToken.id.parseID()){
                // Admins and the user involved in the process are authorized
            return
        }

        throw ForbiddenException("Only an admin or the user related to this information are authorized.")
    }

    fun createUser(registrationRequest: RegistrationRequest){
        if (userRepository.findByUsername(registrationRequest.username) != null) {
            throw BadRequestException("Username already in use")
        }
        if(userRepository.findByEmail(registrationRequest.email) != null) {
            throw BadRequestException("Email already in use")
        }
        if (registrationRequest.password != registrationRequest.confirmPassword) {
            throw BadRequestException("Passwords do not match")
        }

        val user = User(
            registrationRequest.username,
            passwordEncoder.encode(registrationRequest.password),
            registrationRequest.email,
            Rolename.CUSTOMER.toString(),
            registrationRequest.name,
            registrationRequest.surname,
            registrationRequest.deliveryAddress,
            false
        )

        val savedUser = userRepository.save(user)

        val token = notificationService.createEmailVerificationToken(savedUser)

//        println(token.token)
        val userDTO = savedUser.toDTO()

        val mailSubject = "Email verification"
        
        val mailBody = "Hi ${userDTO.name} ${userDTO.surname},\n" +
                "thank you for signing in to LARA-ecommerce! " +
                "please verify your account by clicking on the link " +
                "http://localhost:8080/auth/registrationConfirm?token=${token.token} \n" +
                "Pay attention, this link will remain active up to 30 minutes."


        val message = MailDTO(
            userDTO.id,
            userDTO.email,
            mailSubject,
            mailBody
        )

        messageService.publish(
            message,
            "SendEmail",
            mailTopic
        )
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

    fun loadUserById(userId: Long): UserDetailsDTO {
        verifyIfAdminOrSameUser(userId)

        val user = findUserById(userId)
        return user.toDTO()
    }

    fun loadUserEmailById(userId: Long): String {
        // No authorization check, this route is only accessible by internal microservices

        val user = findUserById(userId)
        return user.email
    }

    fun loadUserRolesById(userId: Long): Set<Rolename> {
        verifyIfAdminOrSameUser(userId)

        val user = findUserById(userId).toDTO()
        return user.authorities
    }

    fun setPassword(userId: Long, oldPassword: String, newPassword: String) {
        if(!verifyPassword(userId, oldPassword)){
            throw ForbiddenException("User and password provided do not match")
        }

        verifyIfAdminOrSameUser(userId)

        val user = findUserById(userId)
        user.password = passwordEncoder.encode(newPassword)
        userRepository.save(user)
    }

    fun verifyPassword(userId: Long, password: String): Boolean{
        val user = findUserById(userId)
        return passwordEncoder.matches(password, user.password)
    }

    @RolesAllowed("T(it.polito.wa2.ecommerce.common.Rolename).ADMIN")
    fun upgradeToAdmin(userId: Long, newRoles: Set<Rolename>) {
        val user = findUserById(userId)

        newRoles.forEach{
            user.addRole(it)
        }

        userRepository.save(user)
    }

    fun setDeliveryAddress(userId: Long, deliveryAddress: String) {
        val user = findUserById(userId)
        user.deliveryAddress = deliveryAddress
        userRepository.save(user)
    }

     @PreAuthorize("hasAuthority(T(it.polito.wa2.ecommerce.common.Rolename).ADMIN)")
    fun setIsEnabledByUsername(newIsEnabled: Boolean, username: String){
        val user = findUserByUsername(username)
        user.isEnabled = newIsEnabled
        userRepository.save(user)
    }

    private fun findUserByUsername(username: String): User {
        return userRepository.findByUsername(username) ?: throw UsernameNotFoundException("Username not found")
    }

    private fun findUserById(userId: Long): User {
        return userRepository.findByIdOrNull(userId) ?: throw UsernameNotFoundException("User ID not found")
    }

    fun verifyToken(token: String) {
        val verificationToken = notificationService.verifyToken(token)

        val username = verificationToken.user!!.username

        setIsEnabledByUsername(true, username)
        addRoleByUsername(Rolename.CUSTOMER, username)

        notificationService.deleteToken(verificationToken)
    }
}