package it.polito.wa2.ecommerce.userservice.repository

import it.polito.wa2.ecommerce.userservice.domain.EmailVerificationToken
import it.polito.wa2.ecommerce.userservice.domain.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
interface EmailVerificationTokenRepository: CrudRepository<EmailVerificationToken, Long> {

    @Transactional(readOnly = true)
    fun findByToken(token: String): EmailVerificationToken?

    @Transactional(readOnly = true)
    fun findByUser(user: User): EmailVerificationToken?

    @Transactional(readOnly = true)
    fun getEmailVerificationTokensByExpiryDateIsBefore(date: Date): List<EmailVerificationToken?>

    @Transactional(readOnly = false)
    fun deleteAllByExpiryDateIsBefore(date: Date)
}