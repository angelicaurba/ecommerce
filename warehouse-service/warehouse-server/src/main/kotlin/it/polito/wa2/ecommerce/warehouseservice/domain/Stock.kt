package it.polito.wa2.ecommerce.warehouseservice.domain

// import it.polito.wa2.ecommerce.catalogueservice.domain.Product
import it.polito.wa2.ecommerce.warehouseservice.client.StockDTO
import it.polito.wa2.ecommerce.common.EntityBase
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.ManyToOne
import javax.validation.constraints.NotNull

@Entity
class Stock (
    @ManyToOne
    val warehouse: Warehouse,

    @field:NotNull
    @Column(unique=true)
    val product: String,

    @field:NotNull
    @Column
    val quantity: Long,

    @field:NotNull
    @Column
    val alarm: Long
) : EntityBase<Long>(){

    fun toDTO(): StockDTO {
        TODO("implement")
        return StockDTO(warehouse.getId(), product, quantity, alarm)
    }
}