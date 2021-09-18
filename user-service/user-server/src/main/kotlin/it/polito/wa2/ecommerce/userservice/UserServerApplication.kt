package it.polito.wa2.ecommerce.userservice

import it.polito.wa2.ecommerce.common.CommonApplication
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


@SpringBootApplication(scanBasePackages = [
    "it.polito.wa2.ecommerce.userservice",
    "it.polito.wa2.ecommerce.common.exceptions",
    "it.polito.wa2.ecommerce.common.security",
    "it.polito.wa2.ecommerce.common.saga"
],
    scanBasePackageClasses = [
        CommonApplication::class
    ])
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
                "customer1",
                "{bcrypt}\$2a\$10\$Cjn8j9vyjUYNsnMU4d0upeEHR2vdzB0P0cuVoTrrRIttVTGVKJn6W",
                "mail@mail.mail",
                "CUSTOMER",
                "Andrea",
                "Bruno",
                "742 Evergreen Terrace",
                true
            )
            val user2 = User(
                "customer2",
                "{bcrypt}\$2a\$10\$DeWA2pb7T3quohYitK6PUOsyctiMjUCWELykFhhNrjWbQFY0Qbebe",
                "boh@boh.boh",
                "CUSTOMER",
                "Andrea2",
                "Bruno2",
                "Via d'estinzione",
                true
            )
            val user3 = User(
                "adminAccount",
                "{bcrypt}\$2a\$10\$.dGWqRYiFDMrsgo6LsutFutRgO4TiGbcV6zaW/EXnJXvVEjb/qXoC",
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
