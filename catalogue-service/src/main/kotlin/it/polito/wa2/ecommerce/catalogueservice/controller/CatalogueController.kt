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
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
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
    ): List<ProductDTO> {
        if(category != null){
            isACategoryOrThrowBadRequest(category)
            return productService.getProductsByCategory(Category.valueOf(category), pageIdx, pageSize)
        }else
            return productService.getProducts(pageIdx, pageSize)

    }

    @GetMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    fun getProductById(@PathVariable("productId") productId: String): ProductDTO {
        return productService.getProductById(productId)
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    fun addProduct(
        @RequestBody @Valid productRequest: ProductRequestDTO
    ): ProductDTO {
        return productService.addProduct(productRequest)
    }

    @PutMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    fun updateOrCreateProduct(
        @PathVariable("productId") productId: String,
        @RequestBody @Valid  productRequest: ProductRequestDTO
    ): ProductDTO {
        return productService.updateOrCreateProduct(productId, productRequest)
    }

    @PatchMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    fun updateProductFields(
        @PathVariable("productId") productId: String,
        @RequestBody productRequest: ProductRequestDTO
    ): ProductDTO {
        // TODO fare il check a mano che il prezzo sia maggiore di 0
        //  oppure mettere il @valid e cercare di capire che errore mi ha dato
        return productService.updateProductFields(productId, productRequest)
    }

    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    fun deleteProduct(
        @PathVariable("productId") productId: String
    ) {
        productService.deleteProduct(productId)
    }

    @GetMapping("/{productId}/picture")
    @ResponseStatus(HttpStatus.OK)
    fun getPictureByProductId(@PathVariable("productId") productId: String): ResponseEntity<Any> {
        return photoService.getPictureByProductId(productId)
    }

    @PostMapping("/{productId}/picture")
    @ResponseStatus(HttpStatus.OK)
    fun updatePictureByProductId(
        @PathVariable("productId") productId: String,
        @RequestHeader format: String,
        @RequestBody file: MultipartFile
    ) {
        photoService.updatePictureByProductId(productId, format, file)
    }

    @GetMapping("/{productId}/warehouses")
    @ResponseStatus(HttpStatus.OK)
    fun getWarehousesContainingProduct(@PathVariable("productId") productId:String): List<String>{
        TODO("implement")
    }

    @PostMapping("/{productId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    fun addComment(@PathVariable("productId") productId: String,
                   @RequestBody comment: AddCommentDTO,
                   bindingResult: BindingResult
    ): ProductDTO{
        if (!bindingResult.hasErrors())
            return commentService.addComment(productId, comment)
        else throw BadRequestException("Bad add comment request")
    }

    @GetMapping("/{productId}/comments")
    @ResponseStatus(HttpStatus.OK)
    fun getComments(@PathVariable("productId") productId: String): List<CommentDTO>{
        return commentService.getCommentsByProductId(productId)
    }

    private fun isACategoryOrThrowBadRequest(category: String){
        if (! Category.values().map{it.toString()}.contains(category) ) throw Exception()
        // TODO throw the correct exception
    }

}