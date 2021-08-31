package it.polito.wa2.ecommerce.orderservice.repository

import it.polito.wa2.ecommerce.orderservice.domain.Order
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.awt.print.Pageable

@Repository
interface OrderRepository : PagingAndSortingRepository<Order, Long>{

    @Transactional(readOnly = true)
    fun findAllByBuyerId(buyerId : String, page: PageRequest): Page<Order>
}
