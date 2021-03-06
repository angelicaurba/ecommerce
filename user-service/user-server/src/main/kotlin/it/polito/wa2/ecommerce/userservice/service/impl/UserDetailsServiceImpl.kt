package it.polito.wa2.ecommerce.userservice.service.impl

import it.polito.wa2.ecommerce.common.security.utils.JwtUtils
import it.polito.wa2.ecommerce.common.Rolename
import it.polito.wa2.ecommerce.common.constants.mailTopic
import it.polito.wa2.ecommerce.common.constants.walletCreationTopic
import it.polito.wa2.ecommerce.common.exceptions.BadRequestException
import it.polito.wa2.ecommerce.common.exceptions.ForbiddenException
import it.polito.wa2.ecommerce.common.exceptions.NotFoundException
import it.polito.wa2.ecommerce.common.parseID
import it.polito.wa2.ecommerce.common.saga.service.MessageService
import it.polito.wa2.ecommerce.common.security.JwtTokenDetails
import it.polito.wa2.ecommerce.common.security.UserDetailsDTO
import it.polito.wa2.ecommerce.mailservice.client.MailDTO
import it.polito.wa2.ecommerce.userservice.domain.User
import it.polito.wa2.ecommerce.userservice.repository.UserRepository
import it.polito.wa2.ecommerce.userservice.service.NotificationService
import it.polito.wa2.ecommerce.userservice.client.RegistrationRequestDTO
import it.polito.wa2.ecommerce.userservice.client.UserUpdateRequestDTO
import it.polito.wa2.ecommerce.walletservice.client.wallet.request.CustomerWalletCreationRequestDTO
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

    fun createUser(registrationRequestDTO: RegistrationRequestDTO){
        if (userRepository.findByUsername(registrationRequestDTO.username) != null) {
            throw BadRequestException("Username already in use")
        }
        if(userRepository.findByEmail(registrationRequestDTO.email) != null) {
            throw BadRequestException("Email already in use")
        }
        if (registrationRequestDTO.password != registrationRequestDTO.confirmPassword) {
            throw BadRequestException("Passwords do not match")
        }

        val user = User(
            registrationRequestDTO.username,
            passwordEncoder.encode(registrationRequestDTO.password),
            registrationRequestDTO.email,
            Rolename.CUSTOMER.toString(),
            registrationRequestDTO.name,
            registrationRequestDTO.surname,
            registrationRequestDTO.deliveryAddress,
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

    fun updateUserFields(userId: Long, updateRequest: UserUpdateRequestDTO): UserDetailsDTO{
        val user = findUserById(userId)

        verifyIfAdminOrSameUser(userId)

        updateRequest.name?.also { user.name = it }
        updateRequest.surname?.also { user.surname = it }
        updateRequest.deliveryAddress?.also { user.deliveryAddress = it }

        return userRepository.save(user).toDTO()
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
        return userRepository.findByIdOrNull(userId) ?: throw NotFoundException("User ID not found")
    }

    fun verifyToken(token: String) {
        val verificationToken = notificationService.verifyToken(token)

        val username = verificationToken.user!!.username

        setIsEnabledByUsername(true, username)
        addRoleByUsername(Rolename.CUSTOMER, username)

        notificationService.deleteToken(verificationToken)

        // Create wallet when the user is created
        val customerWalletRequest = CustomerWalletCreationRequestDTO(
            verificationToken.user!!.getId().toString()
        )
        messageService.publish(
            customerWalletRequest,
            "CustomerWalletCreation",
            walletCreationTopic
        )
    }
}