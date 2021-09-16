package it.polito.wa2.ecommerce.catalogueservice.gateway

import it.polito.wa2.ecommerce.catalogueservice.service.ProductService
import it.polito.wa2.ecommerce.orderservice.client.item.ItemDTO
import it.polito.wa2.ecommerce.orderservice.client.item.PurchaseItemDTO
import it.polito.wa2.ecommerce.orderservice.client.order.request.OrderRequestDTO
import org.reactivestreams.Publisher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class AddPriceToItem : RewriteFunction<OrderRequestDTO<ItemDTO>, OrderRequestDTO<PurchaseItemDTO>> {

    @Autowired
    lateinit var productService: ProductService

    override fun apply(t: ServerWebExchange?, u: OrderRequestDTO<ItemDTO>): Publisher<OrderRequestDTO<PurchaseItemDTO>> {
        val newOrderRequest = OrderRequestDTO<PurchaseItemDTO>(
            u.buyerId,
            u.buyerWalletId,
            u.address,
            u.deliveryItems
                .map { item ->
                    PurchaseItemDTO(
                        item.productId,
                        item.amount,
                        productService.getProductById(item.productId).price
                    )
                }
        )

        return Mono.just(newOrderRequest)

    }

}