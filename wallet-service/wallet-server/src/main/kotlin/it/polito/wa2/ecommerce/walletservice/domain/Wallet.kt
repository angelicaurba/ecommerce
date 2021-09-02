package it.polito.wa2.ecommerce.walletservice.domain

import it.polito.wa2.ecommerce.common.EntityBase
import it.polito.wa2.ecommerce.walletservice.client.wallet.CustomerWalletDTO
import it.polito.wa2.ecommerce.walletservice.client.wallet.WalletDTO
import it.polito.wa2.ecommerce.walletservice.client.wallet.WarehouseWalletDTO
import java.math.BigDecimal
import javax.persistence.*
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.Digits
import javax.validation.constraints.NotNull

@Entity
class Wallet(
    @Column(nullable=false)
    @field:NotNull
    var owner:String,
    @Column(nullable=false)
    @field:NotNull
    var walletType: WalletType,
    @Column(nullable=false)
    @field:NotNull
    @field:DecimalMin("0.00", inclusive= true)
    @field:Digits(fraction=2, integer = 10)
    var amount: BigDecimal=BigDecimal("0.00")
): EntityBase<Long>(){
    fun toDTO(): WalletDTO {
        return when(walletType){
            WalletType.WAREHOUSE-> WarehouseWalletDTO(getId().toString(),owner, amount)
            WalletType.CUSTOMER-> CustomerWalletDTO(getId().toString(), owner, amount)
        }
    }
}

enum class WalletType{
    CUSTOMER, WAREHOUSE
}