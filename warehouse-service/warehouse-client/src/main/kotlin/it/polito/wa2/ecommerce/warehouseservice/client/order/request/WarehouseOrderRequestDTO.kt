package it.polito.wa2.ecommerce.warehouseservice.client.order.request

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import it.polito.wa2.ecommerce.common.saga.domain.Emittable

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "requestType")
@JsonSubTypes(
    value =  [
        JsonSubTypes.Type(value = WarehouseOrderRequestNewDTO::class,  name = "NEW"),
        JsonSubTypes.Type(value = WarehouseOrderRequestCancelDTO::class,  name = "CANCEL")
    ])

interface WarehouseOrderRequestDTO : Emittable{
    val orderId: String
    val requestType: RequestType

    override fun getId(): String {
        return orderId
    }
}

enum class RequestType{
    CANCEL, NEW
}