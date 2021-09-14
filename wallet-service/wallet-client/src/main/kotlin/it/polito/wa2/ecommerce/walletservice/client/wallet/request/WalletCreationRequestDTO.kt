package it.polito.wa2.ecommerce.walletservice.client.wallet.request

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import it.polito.wa2.ecommerce.common.saga.domain.Emittable

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "walletType")
@JsonSubTypes(
value =  [
    JsonSubTypes.Type(value = WarehouseWalletCreationRequestDTO::class,  name="WAREHOUSE"),
    JsonSubTypes.Type(value = CustomerWalletCreationRequestDTO::class, name = "CUSTOMER")
        ])
interface WalletCreationRequestDTO: Emittable{
    val walletType:String
}