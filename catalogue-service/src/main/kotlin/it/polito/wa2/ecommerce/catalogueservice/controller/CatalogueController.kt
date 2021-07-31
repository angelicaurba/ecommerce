package it.polito.wa2.ecommerce.catalogueservice.controller

import it.polito.wa2.ecommerce.catalogueservice.domain.Product
import it.polito.wa2.ecommerce.catalogueservice.dto.ProductDTO
import it.polito.wa2.ecommerce.catalogueservice.dto.ProductRequestDTO
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.validation.Valid
import javax.ws.rs.QueryParam

@RestController
@RequestMapping("/products")
class CatalogueController {

    @GetMapping("/")
    fun getProductsByCategory(@QueryParam("category") category: String): List<Product> {
        TODO("implement")
    }

    @GetMapping("/{productId}")
    fun getProductById(@PathVariable("productId") productId: String): ProductDTO {
        TODO("implement")
    }

    @PostMapping("/")
    fun addProduct(
        @RequestBody @Valid  productRequest: ProductRequestDTO,
        result: BindingResult
    ): ProductDTO {
        TODO("implement")
    }

    @PutMapping("/{productId}")
    fun updateOrCreateProduct(
        @PathVariable("productId") productId: String,
        @RequestBody @Valid  productRequest: ProductRequestDTO,
        result: BindingResult
    ): ProductDTO {
        TODO("implement")
    }

    @PatchMapping("/{productId}")
    fun updateProductFields(
        @PathVariable("productId") productId: String,
        @RequestBody @Valid  productRequest: ProductRequestDTO,
        result: BindingResult
    ): ProductDTO {
        TODO("implement")
    }

    @DeleteMapping("/{productId}")
    fun deleteProduct(
        @PathVariable("productId") productId: String,
        @RequestBody @Valid  productRequest: ProductRequestDTO,
        result: BindingResult
    ) {
        TODO("implement")
    }

    @GetMapping("/{productId}/picture")
    fun getPictureByProductId(@PathVariable("productId") productId: String): ResponseEntity<Any> {
        TODO("implement: see demoimg example")
    }

    @PostMapping("/{productId}/picture")
    fun updatePictureByProductId(
        @PathVariable("productId") productId: String,
        @RequestHeader format: String,
        @RequestBody file: MultipartFile
    ) {
        TODO("implement: see demoimg example")
    }

    @GetMapping("/{productId}/warehouses")
    fun getWarehousesContainingProduct(@PathVariable("productId") productId:String): List<String>{
        TODO("implement")
    }


    // TODO("aggiungere post per aggiungere un commento a un prodotto?")
    // TODO("separare collection per fare ua get dei commenti di un prodotto? (gne)")

}