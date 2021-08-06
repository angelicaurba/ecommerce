package it.polito.wa2.ecommerce.walletservice.domain

import it.polito.wa2.ecommerce.common.EntityBase
import it.polito.wa2.ecommerce.walletservice.client.CustomerWalletDTO
import it.polito.wa2.ecommerce.walletservice.client.WalletDTO
import it.polito.wa2.ecommerce.walletservice.client.WarehouseWalletDTO
import java.math.BigDecimal
import javax.persistence.*

@Entity
class Wallet(
    @Column(nullable=false)
    var owner:String,
    @Column(nullable=false)
    var walletType: WalletType,
    @Column(nullable=false)
    //TODO study precision of bigdecimal
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