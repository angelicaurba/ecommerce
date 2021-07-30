package it.polito.wa2.ecommerce.walletservice.domain

import java.math.BigDecimal
import javax.persistence.*

@Entity
//TODO define entity base
class Wallet(

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null,
    var owner:String,
    var walletType: WalletType,
    var amount: BigDecimal,
    @OneToMany
    var transactions:List<Transaction>

){}

enum class WalletType{
    CUSTOMER, WAREHOUSE
}