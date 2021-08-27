package it.polito.wa2.ecommerce.walletservice.client.order.request

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import it.polito.wa2.ecommerce.common.saga.domain.Emittable


@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "requestType")
@JsonSubTypes(
    value =  [
        JsonSubTypes.Type(value = WarehouseOrderPaymentRequestDTO::class,  name = "PAY"),
        JsonSubTypes.Type(value = WarehouseOrderRefundRequestDTO::class,  name = "REFUND")
    ])
interface WarehouseOrderRequestDTO: Emittable {
    val walletFrom: String
    val userId: String
    val orderId: String
    val requestType: OrderPaymentType

    override fun getId(): String {
        return orderId
    }
}

enum class OrderPaymentType{
    PAY,
    REFUND
}