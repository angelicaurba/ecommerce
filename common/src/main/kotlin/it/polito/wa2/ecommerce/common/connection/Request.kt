package it.polito.wa2.ecommerce.common.connection

import it.polito.wa2.ecommerce.common.exceptions.ServiceUnavailable
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.core.publisher.switchIfEmpty

@Component
class Request {

    @Autowired
    lateinit var webClientBuilder: WebClient.Builder

    fun <T> doGet(uri: String, className: Class<T>): T{
        val returnValue: T?

        try {
            returnValue = webClientBuilder.build()
                .get()
                .uri(uri)
                .retrieve()
                .bodyToMono(className)
                .block()
        }
        catch (e: Exception){
            println(e.message)
            throw ServiceUnavailable("Error during connection with other server")
        }

        return returnValue ?: throw ServiceUnavailable("No response from other server")
    }

    fun <T> doGetReactive(uri: String, className: Class<T>): Mono<T>{
        val returnValue: Mono<T>

        try {
            returnValue = webClientBuilder.build()
                .get()
                .uri(uri)
                .retrieve()
                .bodyToMono(className)
        }
        catch (e: Exception){
            println(e.message)
            return Mono.error(ServiceUnavailable("Error during connection with other server"))
        }

        return returnValue.switchIfEmpty (
                Mono.error(ServiceUnavailable("No response from other server"))
        )
    }

    fun <T: Any> doPost(uri: String, requestBody: T, className: Class<T>){
        try {
            webClientBuilder.build()
                .post()
                .uri(uri)
                .body(Mono.just(requestBody), className)
                .retrieve()
                .bodyToMono(String::class.java)
                .block()
        }
        catch (e: Exception){
            println(e.message)
            throw ServiceUnavailable("Error during connection with other server")
        }
    }

}