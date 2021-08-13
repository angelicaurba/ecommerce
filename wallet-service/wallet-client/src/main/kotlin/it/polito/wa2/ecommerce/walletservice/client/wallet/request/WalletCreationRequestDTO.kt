package it.polito.wa2.ecommerce.walletservice.client.wallet.request

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type")
@JsonSubTypes(
value =  [
    JsonSubTypes.Type(value = WarehouseWalletCreationRequestDTO::class,  name="warehouse"),
    JsonSubTypes.Type(value = CustomerWalletCreationRequestDTO::class, name = "cat")
        ])
interface WalletCreationRequestDTO