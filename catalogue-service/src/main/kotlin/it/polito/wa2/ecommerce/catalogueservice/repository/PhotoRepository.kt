package it.polito.wa2.ecommerce.catalogueservice.repository

import it.polito.wa2.ecommerce.catalogueservice.domain.Photo
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono
import java.util.*

@Repository
interface PhotoRepository : ReactiveMongoRepository<Photo, String>{

    @Transactional(readOnly = true)
    fun findPhotoByProductId(productId: String): Mono<Photo>
}