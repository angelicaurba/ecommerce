package it.polito.wa2.ecommerce.catalogueservice.controller


import it.polito.wa2.ecommerce.common.ErrorMessageDTO
import it.polito.wa2.ecommerce.common.exceptions.BadRequestException
import it.polito.wa2.ecommerce.common.exceptions.ForbiddenException
import it.polito.wa2.ecommerce.common.exceptions.NotFoundException
import it.polito.wa2.ecommerce.common.exceptions.ServiceUnavailable
import org.springframework.dao.DuplicateKeyException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.bind.support.WebExchangeBindException
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.ServerWebInputException
import reactor.core.publisher.Mono
import javax.validation.ConstraintViolationException

@RestControllerAdvice
class ControllerAdvisor {

    @ResponseStatus(value= HttpStatus.BAD_REQUEST)
    @ExceptionHandler( value=[BadRequestException::class, ConstraintViolationException::class,
        WebExchangeBindException::class, ServerWebInputException::class, DuplicateKeyException::class] )
    fun badRequestExceptionHandler( e: Exception, serverWebExchange: ServerWebExchange): Mono<ErrorMessageDTO> {
        return Mono.just(ErrorMessageDTO(e, HttpStatus.BAD_REQUEST, serverWebExchange.request.path.toString()))
    }

    @ResponseStatus(value= HttpStatus.NOT_FOUND)
    @ExceptionHandler( NotFoundException::class )
    fun notFoundExceptionHandler( e: Exception, serverWebExchange: ServerWebExchange): Mono<ErrorMessageDTO> {
        return Mono.just(ErrorMessageDTO(e, HttpStatus.NOT_FOUND, serverWebExchange.request.path.toString()))
    }

    @ResponseStatus(value= HttpStatus.FORBIDDEN)
    @ExceptionHandler( value = [ForbiddenException::class] )
    fun forbiddenExceptionHandler( e: Exception, serverWebExchange: ServerWebExchange): Mono<ErrorMessageDTO> {
        return Mono.just(ErrorMessageDTO(e, HttpStatus.FORBIDDEN, serverWebExchange.request.path.toString()))
    }

    @ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler( value = [ServiceUnavailable::class] )
    fun serviceUnavailableExceptionHandler(serverWebExchange: ServerWebExchange, e: Exception): Mono<ErrorMessageDTO> {
        return Mono.just(ErrorMessageDTO(e, HttpStatus.SERVICE_UNAVAILABLE, serverWebExchange.request.path.toString()))
    }
}