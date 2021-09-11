package it.polito.wa2.ecommerce.catalogueservice.service.impl

import it.polito.wa2.ecommerce.catalogueservice.domain.Photo
import it.polito.wa2.ecommerce.catalogueservice.exceptions.ProductNotFoundException
import it.polito.wa2.ecommerce.catalogueservice.repository.PhotoRepository
import it.polito.wa2.ecommerce.catalogueservice.repository.ProductRepository
import it.polito.wa2.ecommerce.catalogueservice.service.PhotoService
import it.polito.wa2.ecommerce.catalogueservice.service.ProductService
import it.polito.wa2.ecommerce.common.exceptions.NotFoundException
import org.bson.BsonBinarySubType
import org.bson.types.Binary
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
@Transactional
class PhotoServiceImpl : PhotoService{
    @Autowired lateinit var photoRepository: PhotoRepository
    @Autowired lateinit var productService: ProductService

    override fun getPictureByProductId(productId: String): ResponseEntity<Any> {
        productService.getProductByIdOrThrowException(productId)
        val result = photoRepository.findPhotoByProductId(productId)

        if(!result.isPresent){
            throw NotFoundException("There is no photo for product $productId")
        }

        val format = "image/" + result.get().format
        val image = result.get().image

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(format))
            .body(image.data)
    }

    @PreAuthorize("hasAuthority(T(it.polito.wa2.ecommerce.common.Rolename).ADMIN)")
    override fun updatePictureByProductId(productId: String, format: String, file: MultipartFile) {
        productService.getProductByIdOrThrowException(productId)
        val newPhoto = Photo(null, format,
            Binary(BsonBinarySubType.BINARY, file.bytes),
            productId
        )

        photoRepository.save(newPhoto)
    }
}