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
    var name: String,

    @field:NotNull
    @Column
    var address: String,

    @field:NotNull
    @Column
    var adminID: String

): EntityBase<String>(){
    fun toDTO(): WarehouseDTO {
        return WarehouseDTO(getId().toString(), name, address, adminID)
    }
}