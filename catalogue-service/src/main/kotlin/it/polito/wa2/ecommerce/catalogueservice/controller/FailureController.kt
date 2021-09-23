package it.polito.wa2.ecommerce.catalogueservice.controller

import it.polito.wa2.ecommerce.catalogueservice.exceptions.ProductNotFoundException
import it.polito.wa2.ecommerce.common.ErrorMessageDTO
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@RestController
class FailureController{

    @GetMapping("/failure")
    @ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
    fun reactToFailureGET(exchange: ServerWebExchange): Mono<ErrorMessageDTO> {
        val e: java.lang.Exception? =
            exchange.getAttribute(ServerWebExchangeUtils.CIRCUITBREAKER_EXECUTION_EXCEPTION_ATTR)

        if (e != null && e is ProductNotFoundException)
            throw e

        return Mono.just(ErrorMessageDTO(Exception("Unable to connect"), HttpStatus.GATEWAY_TIMEOUT, ""))
    }

    @PostMapping("/failure")
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    fun reactToFailurePOST(exchange: ServerWebExchange): Mono<ErrorMessageDTO> {
        val e: java.lang.Exception? =
            exchange.getAttribute(ServerWebExchangeUtils.CIRCUITBREAKER_EXECUTION_EXCEPTION_ATTR)

        if (e != null && e is ProductNotFoundException)
            throw e

        return Mono.just(ErrorMessageDTO(Exception("Unable to connect"), HttpStatus.BAD_GATEWAY, ""))
    }
}