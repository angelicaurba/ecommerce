package it.polito.wa2.ecommerce.walletservice.client.order.request

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo


@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "requestType")
@JsonSubTypes(
    value =  [
        JsonSubTypes.Type(value = OrderRequestDTO::class,  name = "PAY"), //TODO test this
        JsonSubTypes.Type(value = RefundRequestDTO::class,  name = "REFUND")
    ])
interface OrderRequestDTO {
    val walletFrom: String
    val userId: String
    val orderId: String
    val requestType: OrderPaymentType
}

enum class OrderPaymentType{
    PAY,
    REFUND
}