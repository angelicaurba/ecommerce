package it.polito.wa2.ecommerce.catalogueservice.service

import org.bson.types.Binary
import org.springframework.http.ResponseEntity
import org.springframework.web.multipart.MultipartFile
import reactor.core.publisher.Mono

interface PhotoService {
    fun getPictureByProductId(productId: String): Mono<ResponseEntity<Any>>
    fun updatePictureByProductId(productId: String, format: String, file: Mono<Binary>)
}