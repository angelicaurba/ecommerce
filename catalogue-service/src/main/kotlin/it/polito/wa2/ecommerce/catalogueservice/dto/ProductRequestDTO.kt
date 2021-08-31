package it.polito.wa2.ecommerce.catalogueservice.dto

import it.polito.wa2.ecommerce.catalogueservice.domain.Category
import java.math.BigDecimal
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.Digits
import javax.validation.constraints.NotNull

data class ProductRequestDTO(
    @field:NotNull
    val name: String? = null,
    @field:NotNull
    val description: String? = null,
    @field:NotNull
    val category: Category? = null,
    @field:NotNull
    @field:DecimalMin("0.00", inclusive= true)
    @field:Digits(fraction=2, integer = 10)
    val price: BigDecimal? = null
)
