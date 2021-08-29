package it.polito.wa2.ecommerce.orderservice.it.polito.wa2.ecommerce.orderservice.repository

import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface PurchaseItemRepository : PagingAndSortingRepository<PurchaseItemRepository, Long>{

}
