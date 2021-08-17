package it.polito.wa2.ecommerce.warehouseservice.controller

import it.polito.wa2.ecommerce.warehouseservice.client.WarehouseDTO
import it.polito.wa2.ecommerce.warehouseservice.service.WarehouseService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

@RestController
@RequestMapping("/warehouses")
@Validated
class WarehouseController {

    @Autowired
    lateinit var warehouseService: WarehouseService


    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    fun getAllWarehouses(): List<WarehouseDTO>{
        return warehouseService.getAllWarehouses()
    }

    @GetMapping("/{warehouseId}")
    @ResponseStatus(HttpStatus.OK)
    fun getWarehouseById(@PathVariable("warehouseId") @Min(0) warehouseId: Long): WarehouseDTO{
        return warehouseService.getWarehouseById(warehouseId)
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    fun addWarehouse(@RequestBody @Valid warehouse: WarehouseDTO,
                     bindingResult: BindingResult
    ): WarehouseDTO {
        if (bindingResult.hasErrors())
            throw  Exception("error")//BadRequestException(bindingResult.fieldErrors.joinToString())
        return warehouseService.addWarehouse(warehouse)
    }


    @PutMapping("/{warehouseId}")
    @ResponseStatus(HttpStatus.OK)
    fun updateWarehouse(@PathVariable("warehouseId") @Min(0) warehouseId: Long,
                        @RequestBody @NotNull warehouse: WarehouseDTO) : WarehouseDTO {
        return warehouseService.updateWarehouse(warehouseId, warehouse)
    }
    /*

    @PatchMapping("/{warehouseId}")
    @ResponseStatus(HttpStatus.OK)
    fun updateQuantity(@PathVariable("productId") @Min(0) productId: Long,
                       @RequestBody @NotNull quantity: Long) : ProductDTO{
            return warehouseService.updateQuantity(productId, quantity)
    }

    */

    @DeleteMapping("/{warehouseId}")
    @ResponseStatus(HttpStatus.OK)
    fun deleteWarehouseById(@PathVariable("warehouseId") @Min(0) warehouseId: Long): WarehouseDTO{
        return warehouseService.deleteWarehouseById(warehouseId)
    }




}