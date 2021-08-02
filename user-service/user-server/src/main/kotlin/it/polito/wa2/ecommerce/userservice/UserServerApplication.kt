package it.polito.wa2.ecommerce.userservice

import it.polito.wa2.ecommerce.userservice.repository.EmailVerificationTokenRepository
import it.polito.wa2.ecommerce.userservice.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*

const val tokenClearPeriod: Long = 3600000
// 1000 * 60 * 60 * 24 = 86400000 = 1 day
// 1000 * 60 * 60 * 1 = 3600000 = 1 hour

@SpringBootApplication
@EnableScheduling
@EnableGlobalMethodSecurity(prePostEnabled = true)
class Group6Application{

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }

    @Autowired lateinit var emailVerificationTokenRepository: EmailVerificationTokenRepository
    @Autowired lateinit var userRepository: UserRepository

    @Scheduled(fixedRate = tokenClearPeriod)
    fun cleaner(){

        val date = Date()
        val evt = emailVerificationTokenRepository.getEmailVerificationTokensByExpiryDateIsBefore(date)
        /* evt.forEach{
           userRepository.deleteUserByUsernameAndEnabledFalse( it!!.user!!.username )
       }
       */
        userRepository.deleteAll( evt.map{it!!.user}.filter{ !it!!.isEnabled } )
        emailVerificationTokenRepository.deleteAllByExpiryDateIsBefore(date)
    }
}

fun main(args: Array<String>) {
    runApplication<Group6Application>(*args)
}