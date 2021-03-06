package it.polito.wa2.ecommerce.common.exceptions

import it.polito.wa2.ecommerce.common.ErrorMessageDTO
import org.springframework.http.HttpStatus
import org.springframework.web.bind.MissingRequestValueException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.bind.support.WebExchangeBindException
import org.springframework.web.server.ServerWebInputException
import java.sql.SQLIntegrityConstraintViolationException
import javax.servlet.http.HttpServletRequest
import javax.validation.ConstraintViolationException

@RestControllerAdvice
class ControllerAdvisor {

    @ResponseStatus(value= HttpStatus.BAD_REQUEST)
    @ExceptionHandler( value=[BadRequestException::class, ConstraintViolationException::class,
        WebExchangeBindException::class, ServerWebInputException::class, MissingRequestValueException::class,
        SQLIntegrityConstraintViolationException::class] )
    fun badRequestExceptionHandler(req: HttpServletRequest, e: Exception): ErrorMessageDTO {
        return ErrorMessageDTO(e, HttpStatus.BAD_REQUEST, req.requestURI)
    }

    @ResponseStatus(value= HttpStatus.NOT_FOUND)
    @ExceptionHandler( NotFoundException::class )
    fun notFoundExceptionHandler(req: HttpServletRequest, e: Exception): ErrorMessageDTO {
        return ErrorMessageDTO(e, HttpStatus.NOT_FOUND, req.requestURI)
    }

    @ResponseStatus(value= HttpStatus.FORBIDDEN)
    @ExceptionHandler( value = [ForbiddenException::class] )
    fun forbiddenExceptionHandler(req: HttpServletRequest, e: Exception): ErrorMessageDTO {
        return ErrorMessageDTO(e, HttpStatus.FORBIDDEN, req.requestURI)
    }

    @ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler( value = [ServiceUnavailable::class] )
    fun serviceUnavailableExceptionHandler(req: HttpServletRequest, e: Exception): ErrorMessageDTO {
        return ErrorMessageDTO(e, HttpStatus.SERVICE_UNAVAILABLE, req.requestURI)
    }

}