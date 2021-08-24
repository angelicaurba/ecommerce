package it.polito.wa2.ecommerce.catalogueservice.service

import org.springframework.http.ResponseEntity
import org.springframework.web.multipart.MultipartFile

interface PhotoService {
    fun getPictureByProductId(productId: String): ResponseEntity<Any>
    fun updatePictureByProductId(productId: String, format: String, file: MultipartFile)
}