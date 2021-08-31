package it.polito.wa2.ecommerce.orderservice.repository
import it.polito.wa2.ecommerce.orderservice.domain.PurchaseItem
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface PurchaseItemRepository : PagingAndSortingRepository<PurchaseItem, Long>{

}
