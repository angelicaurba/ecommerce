package it.polito.wa2.ecommerce.warehouseservice.repository

import it.polito.wa2.ecommerce.warehouseservice.domain.Stock
import it.polito.wa2.ecommerce.warehouseservice.domain.Warehouse
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface StockRepository : PagingAndSortingRepository<Stock, String> {

    @Transactional(readOnly = true)
    fun findAllByWarehouse(warehouse: Warehouse, page: Pageable): List<Stock>

    @Transactional(readOnly = true)
    fun findByWarehouseAndProduct(warehouse: Warehouse, product: String): Stock?

    @Transactional(readOnly = true)
    fun findAllByProduct(product: String, page: Pageable): List<Stock>

    @Transactional(readOnly = true)
    fun findAllByProductAndQuantityIsGreaterThanEqual(product: String, quantity: Long): List<Stock>

}