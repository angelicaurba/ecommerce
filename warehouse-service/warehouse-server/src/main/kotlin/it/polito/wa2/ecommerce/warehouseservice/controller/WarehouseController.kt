package it.polito.wa2.ecommerce.warehouseservice.controller

import it.polito.wa2.ecommerce.common.exceptions.BadRequestException
import it.polito.wa2.ecommerce.warehouseservice.client.StockDTO
import it.polito.wa2.ecommerce.warehouseservice.client.StockRequestDTO
import it.polito.wa2.ecommerce.warehouseservice.client.WarehouseRequestDTO
import it.polito.wa2.ecommerce.warehouseservice.client.WarehouseDTO
import it.polito.wa2.ecommerce.warehouseservice.service.StockService
import it.polito.wa2.ecommerce.warehouseservice.service.WarehouseService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

const val DEFAULT_PAGE_SIZE = "10"

@RestController
@RequestMapping("/warehouses")
@Validated
class WarehouseController {

    @Autowired
    lateinit var warehouseService: WarehouseService

    @Autowired
    lateinit var stockService: StockService

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    fun getAllWarehouses(
        @RequestParam(value = "productID", required = false) productID: String?,
        @RequestParam("pageIndex", defaultValue = "1") @Min(1) pageIdx: Int,
        @RequestParam("pageSize", defaultValue = DEFAULT_PAGE_SIZE) @Min(1) pageSize: Int
    ) : List<WarehouseDTO> {
        if (productID == null)
            return warehouseService.getAllWarehouses(pageIdx, pageSize)
        else return stockService.getAllWarehousesHavingProduct(productID, pageIdx, pageSize)
    }

    @GetMapping("/{warehouseID}")
    @ResponseStatus(HttpStatus.OK)
    fun getWarehouseById(
        @PathVariable("warehouseID") warehouseId: String
    ) : WarehouseDTO {
        return warehouseService.getWarehouseById(warehouseId)
    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    fun addWarehouse(
        @RequestBody @Valid warehouseRequest: WarehouseRequestDTO,
        bindingResult: BindingResult
    ) : WarehouseDTO {
        if (bindingResult.hasErrors())
            throw  BadRequestException(bindingResult.fieldErrors.joinToString())
        return warehouseService.addWarehouse(warehouseRequest)
    }


    @PutMapping("/{warehouseID}")
    @ResponseStatus(HttpStatus.OK)
    fun updateOrCreateWarehouse(
        @PathVariable("warehouseID") warehouseId: String,
        @RequestBody @Valid @NotNull warehouseRequest: WarehouseRequestDTO
    ) : WarehouseDTO {
        return warehouseService.updateOrCreateWarehouse(warehouseId, warehouseRequest)
    }


    @PatchMapping("/{warehouseID}")
    @ResponseStatus(HttpStatus.OK)
    fun updateWarehouseFields(
        @PathVariable("warehouseID") warehouseId: String,
        @RequestBody @NotNull warehouseRequest: WarehouseRequestDTO
    ) : WarehouseDTO {
        return warehouseService.updateWarehouseFields(warehouseId, warehouseRequest)
    }

    @DeleteMapping("/{warehouseID}")
    @ResponseStatus(HttpStatus.OK)
    fun deleteWarehouseById(
        @PathVariable("warehouseID") warehouseId: String
    ) {
        warehouseService.deleteWarehouseById(warehouseId)
    }

    // STOCK

    @GetMapping("/{warehouseID}/products")
    @ResponseStatus(HttpStatus.OK)
    fun getStocksByWarehouseID(
        @PathVariable("warehouseID") warehouseID: String,
        @RequestParam("pageIndex", defaultValue = "1") @Min(1) pageIdx: Int,
        @RequestParam("pageSize", defaultValue = DEFAULT_PAGE_SIZE) @Min(1) pageSize: Int
    ) : List<StockDTO> {
        return stockService.getStocksByWarehouseID(warehouseID, pageIdx, pageSize)
    }

    @GetMapping("/{warehouseID}/products/{productID}")
    @ResponseStatus(HttpStatus.OK)
    fun getStockByWarehouseIDandProductID(
        @PathVariable("warehouseID") warehouseID: String,
        @PathVariable("productID") productID: String
    ) : StockDTO {
        return stockService.getStockByWarehouseIDandProductID(warehouseID, productID)
    }

    @PostMapping("/{warehouseID}/products")
    @ResponseStatus(HttpStatus.CREATED)
    fun addStock(
        @PathVariable("warehouseID") warehouseID: String,
        @RequestBody @Valid stockRequest: StockRequestDTO,
        bindingResult: BindingResult
    ) : StockDTO {
        if (bindingResult.hasErrors())
            throw BadRequestException(bindingResult.fieldErrors.joinToString())
        if ( warehouseID != stockRequest.warehouseID )
            throw BadRequestException("WarehouseId contains error")
        return stockService.addStock(warehouseID, stockRequest)
    }

    @PutMapping("/{warehouseID}/products/{productID}")
    @ResponseStatus(HttpStatus.OK)
    fun updateOrCreateStock(
        @PathVariable("warehouseID") warehouseId: String,
        @PathVariable("productID") productID: String,
        @RequestBody @Valid @NotNull stockRequestDTO: StockRequestDTO
    ) : StockDTO {
        if ( warehouseId != stockRequestDTO.warehouseID || productID != stockRequestDTO.productID )
            throw BadRequestException("WarehouseId or productId contains error")
        return stockService.updateOrCreateStock(warehouseId, productID, stockRequestDTO)
    }


    @PatchMapping("/{warehouseID}/products/{productID}")
    @ResponseStatus(HttpStatus.OK)
    fun updateStockFields(
        @PathVariable("warehouseID") warehouseId: String,
        @PathVariable("productID") productID: String,
        @RequestBody @NotNull stockRequestDTO: StockRequestDTO
    ) : StockDTO {
        if ( warehouseId != stockRequestDTO.warehouseID || productID != stockRequestDTO.productID )
            throw BadRequestException("WarehouseId or productId contains error")
        return stockService.updateStockFields(warehouseId, productID, stockRequestDTO)
    }

    @DeleteMapping("/{warehouseID}/products/{productID}")
    @ResponseStatus(HttpStatus.OK)
    fun deleteStock(
        @PathVariable("warehouseID") warehouseId: String,
        @PathVariable("productID") productID: String,
    ) {
        stockService.deleteStockByWarehouseIdAndProductId(warehouseId, productID)
    }

}