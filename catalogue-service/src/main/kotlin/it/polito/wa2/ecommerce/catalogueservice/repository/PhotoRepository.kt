package it.polito.wa2.ecommerce.catalogueservice.repository

import it.polito.wa2.ecommerce.catalogueservice.domain.Photo
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.transaction.annotation.Transactional
import java.util.*

interface PhotoRepository : MongoRepository<Photo, String>{

    @Transactional(readOnly = true)
    fun findPhotoByProductId(productId: String): Optional<Photo>
}