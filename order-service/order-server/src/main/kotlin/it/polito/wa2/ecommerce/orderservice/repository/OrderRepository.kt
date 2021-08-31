package it.polito.wa2.ecommerce.orderservice.repository

import it.polito.wa2.ecommerce.orderservice.domain.Order
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderRepository : PagingAndSortingRepository<Order, Long>
