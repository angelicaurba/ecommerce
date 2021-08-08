package it.polito.wa2.ecommerce.userservice.domain

import it.polito.wa2.ecommerce.common.EntityBase
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

const val tokenExpirationTime: Long = 1800000
// 30 * 60 * 1000 = 30 minutes

@Entity
class EmailVerificationToken(

    @JoinColumn(name = "user", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.EAGER, optional = false)
    var user: User?

) : EntityBase<Long>(){

    @field:NotNull(message = "token must be present")
    @Column( nullable = false, unique = true, updatable = false)
    val token: String = UUID.randomUUID().toString()

    @field:NotNull(message = "expiryDate must be present")
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    val expiryDate: Date = Date(System.currentTimeMillis() +  tokenExpirationTime)


    fun hasExpired(): Boolean {

        val delta = this.expiryDate.time - Date().time
        return delta <= 0
    }

}