package it.polito.wa2.ecommerce.userservice.service.impl

import it.polito.wa2.ecommerce.common.exceptions.ForbiddenException
import it.polito.wa2.ecommerce.userservice.domain.EmailVerificationToken
import it.polito.wa2.ecommerce.userservice.domain.User
import it.polito.wa2.ecommerce.userservice.repository.EmailVerificationTokenRepository
import it.polito.wa2.ecommerce.userservice.service.NotificationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional
class NotificationServiceImpl: NotificationService {

    @Autowired
    lateinit var emailVerificationTokenRepository: EmailVerificationTokenRepository

    override fun createEmailVerificationToken(user: User): EmailVerificationToken {
        val evt = EmailVerificationToken(user)

        emailVerificationTokenRepository.save(evt)
        return evt
    }

    override fun verifyToken(token: String): EmailVerificationToken {
        /**
         * retrieve from repository the EmailVerificationToken by the token String.
         * Verify if it is not expired and enable the corresponding username.
         * If the token is not found or it is expired, throw an exception.
         */

        val evt = emailVerificationTokenRepository.findByToken(token)
        if (evt == null || evt.hasExpired()) {
            throw ForbiddenException("Token has expired!")
        }

        return evt
    }

    override fun deleteToken(verificationToken: EmailVerificationToken) {
        emailVerificationTokenRepository.delete(verificationToken)
    }
}