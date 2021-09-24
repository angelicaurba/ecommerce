package it.polito.wa2.ecommerce.catalogueservice.service.impl

import it.polito.wa2.ecommerce.catalogueservice.domain.Photo
import it.polito.wa2.ecommerce.catalogueservice.repository.PhotoRepository
import it.polito.wa2.ecommerce.catalogueservice.service.PhotoService
import it.polito.wa2.ecommerce.catalogueservice.service.ProductService
import it.polito.wa2.ecommerce.common.exceptions.NotFoundException
import org.bson.types.Binary
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.multipart.FilePart
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono

@Service
@Transactional
class PhotoServiceImpl : PhotoService {
    @Autowired
    lateinit var photoRepository: PhotoRepository
    @Autowired
    lateinit var productService: ProductService

    override fun getPictureByProductId(productId: String): Mono<ResponseEntity<Any>> {
        val result = productService.getProductByIdOrThrowException(productId).flatMap {
            photoRepository.findPhotoByProductId(productId)
        }

        return result
            .switchIfEmpty(Mono.error(NotFoundException("There is no photo for product $productId")))
            .map {
                val format = it.format
                val image = it.image

                ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(format))
                    .body(image.data)
            }
    }

    @PreAuthorize("hasAuthority(T(it.polito.wa2.ecommerce.common.Rolename).ADMIN)")
    override fun updatePictureByProductId(productId: String, file: FilePart): Mono<Void> {

        val contentType = file.headers().contentType
        val result = DataBufferUtils.join(file.content())
            .flatMap {

                val bytes = ByteArray(it.readableByteCount())
                it.read(bytes)
                DataBufferUtils.release(it)

                val newPhoto = Photo(
                    null, contentType.toString(),
                    Binary(bytes),
                    productId
                )
                photoRepository.save(newPhoto)
            }

        return productService.getProductByIdOrThrowException(productId)
            .flatMap {
                photoRepository.deletePhotoByProductId(productId)
                    .then(result)
            }.then()
    }
}