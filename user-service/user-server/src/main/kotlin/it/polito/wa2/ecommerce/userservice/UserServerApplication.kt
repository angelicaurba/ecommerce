package it.polito.wa2.ecommerce.userservice

import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import it.polito.wa2.ecommerce.userservice.domain.User
import it.polito.wa2.ecommerce.userservice.repository.EmailVerificationTokenRepository
import it.polito.wa2.ecommerce.userservice.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
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


@SpringBootApplication(scanBasePackages = ["it.polito.wa2.ecommerce"])
@EnableEurekaClient
@EnableScheduling
@EnableGlobalMethodSecurity(prePostEnabled = true)
class UserServerApplication{

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }

    @Autowired lateinit var emailVerificationTokenRepository: EmailVerificationTokenRepository
    @Autowired lateinit var userRepository: UserRepository

    @Bean
    fun populateDB(): CommandLineRunner {
        return CommandLineRunner {
            emailVerificationTokenRepository.deleteAll()
            userRepository.deleteAll()

            val user1 = User(
                "andrea",
                "{bcrypt}\$2a\$10\$D4i9Smqxyk27dQbp8ssZ0O4FWQnZKR7.N7lgR9b.V8u4TuSzH2gf2",
                "mail@mail.mail",
                "CUSTOMER",
                "Andrea",
                "Bruno",
                "742 Evergreen Terrace",
                true
            )
            val user2 = User(
                "andrea2",
                "{bcrypt}\$2a\$10\$EqrWyhwFRhgwjud7kPG1SOrycfrCQvzUWhHkjkV8kUJQmutRjnvYS",
                "boh@boh.boh",
                "CUSTOMER",
                "Andrea2",
                "Bruno2",
                "Via d'estinzione",
                true
            )
            val user3 = User(
                "admin",
                "{bcrypt}\$2a\$10\$dFtHJEFfmBEkyzBVccdba.Bb0ti7PSOsZ/PlcjHj7K7C85fuE7XSi",
                "admin@admin.admin",
                "CUSTOMER ADMIN",
                "Admin",
                "Root",
                "Root 66",
                true
            )


            userRepository.save(user1)
            userRepository.save(user2)
            userRepository.save(user3)
        }
    }

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
    runApplication<UserServerApplication>(*args)
}
