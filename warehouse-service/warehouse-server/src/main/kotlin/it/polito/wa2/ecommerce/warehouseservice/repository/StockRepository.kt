package it.polito.wa2.ecommerce.warehouseservice.repository

import it.polito.wa2.ecommerce.warehouseservice.domain.Stock
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface StockRepository : PagingAndSortingRepository<Stock, Long> {

}