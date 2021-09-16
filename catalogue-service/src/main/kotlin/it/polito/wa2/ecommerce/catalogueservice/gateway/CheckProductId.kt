package it.polito.wa2.ecommerce.catalogueservice.gateway

import it.polito.wa2.ecommerce.catalogueservice.service.ProductService
import it.polito.wa2.ecommerce.common.exceptions.BadRequestException
import it.polito.wa2.ecommerce.warehouseservice.client.StockRequestDTO
import org.reactivestreams.Publisher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class CheckProductId : RewriteFunction<StockRequestDTO, StockRequestDTO> {

    @Autowired
    lateinit var productService: ProductService

    override fun apply(t: ServerWebExchange?, u: StockRequestDTO): Publisher<StockRequestDTO> {
        if (u.productID == null)
            throw BadRequestException("Stock productID can not be null")

        productService.getProductByIdOrThrowException(u.productID!!)

        return Mono.just(u)
    }

}