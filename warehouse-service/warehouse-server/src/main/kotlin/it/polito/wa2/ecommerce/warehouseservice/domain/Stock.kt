package it.polito.wa2.ecommerce.warehouseservice.domain

import it.polito.wa2.ecommerce.warehouseservice.client.StockDTO
import it.polito.wa2.ecommerce.common.EntityBase
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.ManyToOne
import javax.validation.constraints.NotNull

@Entity
class Stock (
    @ManyToOne
    var warehouse: Warehouse,

    @field:NotNull
    @Column(unique=true)
    var product: String,

    @field:NotNull
    @Column
    var quantity: Long,

    @field:NotNull
    @Column
    var alarm: Long
) : EntityBase<String>(){

    fun toDTO(): StockDTO {
        return StockDTO(getId().toString(),warehouse.getId().toString(), product, quantity, alarm)
    }
}