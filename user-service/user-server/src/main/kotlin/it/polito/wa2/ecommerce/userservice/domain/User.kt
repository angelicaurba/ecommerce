package it.polito.wa2.ecommerce.userservice.domain

import it.polito.wa2.ecommerce.common.EntityBase
import it.polito.wa2.ecommerce.common.Rolename
import it.polito.wa2.ecommerce.userservice.client.UserDetailsDTO
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(indexes = [Index(name = "index", columnList = "username", unique = true)])
class User(
    @field:Size(max = 30, message = "username should have a maximum of 30 characters")
    @field:NotNull(message = "username must be present")
    @Column(nullable=false, unique = true)
    val username: String,

    @field:Size(min = 8, message = "password should have a minimum of 8 characters")
    @field:NotNull(message = "password must be present")
    @Column(nullable=false)
    val password: String,

    @field:Email(message = "email should be valid")
    @field:NotNull(message = "email must be present")
    @Column(nullable=false, unique = true)
    val email: String,

    @field:NotNull(message = "roles must be present")
    @Column(nullable=false)
    var roles: String,

    @field:NotNull(message = "name must be present")
    @Column(nullable=false)
    val name: String,

    @field:NotNull(message = "surname must be present")
    @Column(nullable=false)
    val surname: String,

    @Column(nullable=true)
    var deliveryAddress: String?,

    @field:NotNull(message = "isEnabled must be present")
    @Column(nullable=false)
    var isEnabled: Boolean = false

): EntityBase<Long>(){
    private fun stringToSet(roleString: String): MutableSet<Rolename>{
        if (roleString == "") return mutableSetOf()

        return roleString.split(" ").map{
            Rolename.valueOf(it)
        }.toMutableSet()
    }

    private fun setToString(roleSet: Set<Rolename>): String{
        return roleSet.joinToString(" ")
    }

    fun getRoles(): Set<Rolename>{
        // stringToSet returns a mutable set, but when returned by getRoles, the result is not mutable anymore
        return stringToSet(roles)
    }

    fun addRole(newRole:Rolename){
        val tmpSet = stringToSet(roles)
        tmpSet.add(newRole)
        roles = setToString(tmpSet)
    }

    fun removeRole(oldRole:Rolename){
        val tmpSet = stringToSet(roles)
        tmpSet.remove(oldRole)
        roles = setToString(tmpSet)
    }

    fun toDTO(): UserDetailsDTO {
        return UserDetailsDTO(
            (this.getId()!!).toString(),
            username,
            this.getRoles(),
            password,
            email,
            name,
            surname,
            deliveryAddress,
            isEnabled
        )
    }
}