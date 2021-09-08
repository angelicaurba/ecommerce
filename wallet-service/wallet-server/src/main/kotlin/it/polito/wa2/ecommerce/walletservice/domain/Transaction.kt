package it.polito.wa2.ecommerce.walletservice.domain

import it.polito.wa2.ecommerce.common.EntityBase
import it.polito.wa2.ecommerce.walletservice.client.transaction.TransactionDTO
import org.springframework.data.annotation.CreatedDate
import java.math.BigDecimal
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.ManyToOne
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.Digits
import javax.validation.constraints.NotNull

@Entity
class Transaction (
    @ManyToOne(optional=true)
    var fromWallet:Wallet?, //Will be null in case of recharges
    @ManyToOne(optional=false)
    var toWallet:Wallet?,
    @Column(nullable=false)
    @field:NotNull
    var type:TransactionType,
    @Column(nullable=false)
    @field:NotNull
    @field:DecimalMin("0.00", inclusive= true)
    @field:Digits(fraction=2, integer = 10)
    val amount :BigDecimal,
    @Column(nullable=false)
    @field:NotNull
    val operationReference:String // in case of recharge it should be autogenerated, otherwhise it should be the orderID
        ): EntityBase<Long>(){

    @Column(nullable=false)
    @field:NotNull
    var timestamp: Long = System.currentTimeMillis()

    fun toDTO(): TransactionDTO {
        return TransactionDTO(getId()!!,
            fromWallet?.getId().toString(),
            toWallet!!.getId().toString(),
            amount,
            timestamp, //TODO convert to date for frontend?
            "$type: $operationReference"
        )
    }
}

enum class TransactionType{
    ORDER_PAYMENT,
    ORDER_REFUND,
    RECHARGE
}
