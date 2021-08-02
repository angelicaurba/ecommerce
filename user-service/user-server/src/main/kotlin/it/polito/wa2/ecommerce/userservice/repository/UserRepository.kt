package it.polito.wa2.ecommerce.userservice.repository

import it.polito.wa2.ecommerce.userservice.domain.User
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface UserRepository: CrudRepository<User, Long> {
    @Transactional(readOnly = true)
    fun findByUsername(username: String): User?

    @Transactional(readOnly = true)
    fun findByEmail(email: String): User?

    @Query("delete from User u " +
            "where u.username =:username and u.isEnabled = false "
    )
    @Transactional(readOnly = false)
    fun deleteUserByUsernameAndEnabledFalse(@Param("username") username: String)

}