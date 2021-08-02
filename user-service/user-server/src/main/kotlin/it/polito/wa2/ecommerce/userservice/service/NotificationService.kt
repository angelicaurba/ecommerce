package it.polito.wa2.ecommerce.userservice.service

import it.polito.wa2.ecommerce.userservice.domain.EmailVerificationToken
import it.polito.wa2.ecommerce.userservice.domain.User

interface NotificationService {
    fun createEmailVerificationToken(user: User): EmailVerificationToken
    fun verifyToken(token: String): EmailVerificationToken
    fun deleteToken(verificationToken: EmailVerificationToken)
}