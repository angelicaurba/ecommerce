package it.polito.wa2.ecommerce.warehouseservice.domain

import it.polito.wa2.ecommerce.common.EntityBase
import it.polito.wa2.ecommerce.warehouseservice.client.WarehouseDTO
import javax.persistence.Column
import javax.persistence.Entity
import javax.validation.constraints.NotNull

@Entity
class Warehouse (

    @field:NotNull
    @Column
    val name: String,

    @field:NotNull
    @Column
    val address: String,

    // TODO controllare come settare la relazione
    @field:NotNull
    @Column(unique=true)
    val adminID: String

): EntityBase<Long>(){
    fun toDTO(): WarehouseDTO {
        TODO("implement")
        return WarehouseDTO(getId(), name, address, adminID)
    }
}