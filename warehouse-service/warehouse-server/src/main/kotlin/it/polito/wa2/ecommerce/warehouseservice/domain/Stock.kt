package it.polito.wa2.ecommerce.warehouseservice.domain

import it.polito.wa2.ecommerce.warehouseservice.client.StockDTO
import it.polito.wa2.ecommerce.common.EntityBase
import javax.persistence.*
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["warehouse_id", "prodotto"])])
class Stock (
    @ManyToOne(optional = false, fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "warehouse_id")
    var warehouse: Warehouse,

    @field:NotNull
    @field:Size(min = 1, max = 26)
    @Column(name = "prodotto")
    var product: String,

    @field:NotNull
    @field:Min(0)
    @Column
    var quantity: Long,

    @field:NotNull
    @field:Min(0)
    @Column
    var alarm: Long
) : EntityBase<Long>(){

    fun toDTO(): StockDTO {
        return StockDTO(getId().toString(),warehouse.getId().toString(), product, quantity, alarm)
    }
}