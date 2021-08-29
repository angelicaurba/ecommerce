package it.polito.wa2.ecommerce.orderservice.it.polito.wa2.ecommerce.orderservice.domain

import it.polito.wa2.ecommerce.common.EntityBase
import java.math.BigDecimal
import javax.persistence.Entity

@Entity
class PurchaseItem (
    val productId: String,
    val amount: Int,
    val price: BigDecimal,
    val warehouseId: String?,

) : EntityBase<Long>(){

}


// TODO(add validation and columns)