package it.polito.wa2.ecommerce.catalogueservice.controller

import it.polito.wa2.ecommerce.catalogueservice.domain.Category
import it.polito.wa2.ecommerce.catalogueservice.dto.CommentDTO
import it.polito.wa2.ecommerce.catalogueservice.dto.ProductDTO
import it.polito.wa2.ecommerce.catalogueservice.dto.ProductRequestDTO
import it.polito.wa2.ecommerce.catalogueservice.service.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.validation.Valid
import javax.validation.constraints.Min


const val DEFAULT_PAGE_SIZE = "10"

@RestController
@RequestMapping("/products")
class CatalogueController {

    @Autowired lateinit var productService: ProductService

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    fun getProductsByCategory(@RequestParam("category") category: String,
                              @RequestParam("pageIndex", defaultValue = "1") @Min(1) pageIdx: Int,
                              @RequestParam("pageSize", defaultValue = DEFAULT_PAGE_SIZE) @Min(
                                  1
                              ) pageSize: Int
    ): List<ProductDTO> {
        isACategoryOrThrowBadRequest(category)
        return productService.getProductByCategory(Category.valueOf(category), pageIdx, pageSize)
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
        return productService.getPictureByProductId(productId)
    }

    @PostMapping("/{productId}/picture")
    @ResponseStatus(HttpStatus.OK)
    fun updatePictureByProductId(
        @PathVariable("productId") productId: String,
        @RequestHeader format: String,
        @RequestBody file: MultipartFile
    ) {
        productService.updatePictureByProductId(productId, format, file)
    }

    @GetMapping("/{productId}/warehouses")
    @ResponseStatus(HttpStatus.OK)
    fun getWarehousesContainingProduct(@PathVariable("productId") productId:String): List<String>{
        TODO("implement")
    }

    @PostMapping("/{productId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    fun addComment(@PathVariable("productId") productId: String,
                   @RequestBody comment: CommentDTO,
                   @RequestBody @Valid  productRequest: ProductRequestDTO): ProductDTO{
        return productService.addComment(productId, comment)
    }

    @GetMapping("/{productId}/comments")
    @ResponseStatus(HttpStatus.OK)
    fun getComments(@PathVariable("productId") productId: String): List<CommentDTO>{
        return productService.getCommentsByProductId(productId)
    }

    private fun isACategoryOrThrowBadRequest(category: String){
        if (! Category.values().map{it.toString()}.contains(category) ) throw Exception()
        // TODO throw the correct exception
    }

}