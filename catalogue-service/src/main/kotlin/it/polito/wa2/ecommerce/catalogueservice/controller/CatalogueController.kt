package it.polito.wa2.ecommerce.catalogueservice.controller

import it.polito.wa2.ecommerce.catalogueservice.domain.Category
import it.polito.wa2.ecommerce.catalogueservice.dto.AddCommentDTO
import it.polito.wa2.ecommerce.catalogueservice.dto.CommentDTO
import it.polito.wa2.ecommerce.catalogueservice.dto.ProductDTO
import it.polito.wa2.ecommerce.catalogueservice.dto.ProductRequestDTO
import it.polito.wa2.ecommerce.catalogueservice.service.CommentService
import it.polito.wa2.ecommerce.catalogueservice.service.PhotoService
import it.polito.wa2.ecommerce.catalogueservice.service.ProductService
import it.polito.wa2.ecommerce.common.exceptions.BadRequestException
import org.bson.types.Binary
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import javax.validation.Valid
import javax.validation.constraints.Min


const val DEFAULT_PAGE_SIZE = "10"

@RestController
@RequestMapping("/products")
class CatalogueController {

    @Autowired lateinit var productService: ProductService
    @Autowired lateinit var photoService: PhotoService
    @Autowired lateinit var commentService: CommentService

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    fun getProductsByCategory(@RequestParam("category", required = false) category: String?,
                              @RequestParam("pageIndex", defaultValue = "1") @Min(1) pageIdx: Int,
                              @RequestParam("pageSize", defaultValue = DEFAULT_PAGE_SIZE) @Min(
                                  1
                              ) pageSize: Int
    ): Flux<ProductDTO> {
        if(category != null){
            isACategoryOrThrowBadRequest(category)
            return productService.getProductsByCategory(Category.valueOf(category), pageIdx, pageSize)
        }else
            return productService.getProducts(pageIdx, pageSize)

    }

    @GetMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    fun getProductById(@PathVariable("productId") productId: String): Mono<ProductDTO> {
        return productService.getProductById(productId)
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    fun addProduct(
        @RequestBody @Valid productRequest: Mono<ProductRequestDTO>
    ): Mono<ProductDTO> {
        return productService.addProduct(productRequest)
    }

    @PutMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    fun updateOrCreateProduct(
        @PathVariable("productId") productId: String,
        @RequestBody @Valid  productRequest: Mono<ProductRequestDTO>
    ): Mono<ProductDTO> {
        return productService.updateOrCreateProduct(productId, productRequest)
    }

    @PatchMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    fun updateProductFields(
        @PathVariable("productId") productId: String,
        @RequestBody productRequest: Mono<ProductRequestDTO>
    ): Mono<ProductDTO> {
        return productService.updateProductFields(productId, productRequest)
    }

    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    fun deleteProduct(
        @PathVariable("productId") productId: String
    ) : Mono<Void>{
        return productService.deleteProduct(productId)
    }

    @GetMapping("/{productId}/picture")
    @ResponseStatus(HttpStatus.OK)
    fun getPictureByProductId(@PathVariable("productId") productId: String): Mono<ResponseEntity<Any>> {
        return photoService.getPictureByProductId(productId)
    }

    @PostMapping("/{productId}/picture", consumes = [MediaType.IMAGE_GIF_VALUE, MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE])
    @ResponseStatus(HttpStatus.OK)
    fun updatePictureByProductId(
        @PathVariable("productId") productId: String,
        @RequestHeader("Content-Type") format: String,
        @RequestBody file: Mono<Binary>
    ) : Mono<Void>{
        return photoService.updatePictureByProductId(productId, format, file)
    }

    @GetMapping("/{productId}/warehouses")
    @ResponseStatus(HttpStatus.OK)
    fun getWarehousesContainingProduct(@PathVariable("productId") productId:String): Flux<String>{
        return productService.getWarehousesContainingProduct(productId)
    }

    @PostMapping("/{productId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    fun addComment(@PathVariable("productId") productId: String,
                   @RequestBody @Valid comment: Mono<AddCommentDTO>
    ): Mono<ProductDTO> {
        return commentService.addComment(productId, comment)
    }

    @GetMapping("/{productId}/comments")
    @ResponseStatus(HttpStatus.OK)
    fun getComments(@PathVariable("productId") productId: String): Flux<CommentDTO>{
        return commentService.getCommentsByProductId(productId)
    }

    private fun isACategoryOrThrowBadRequest(category: String){
        if (! Category.values().map{it.toString()}.contains(category) )
            throw BadRequestException("Bad category")
    }

}