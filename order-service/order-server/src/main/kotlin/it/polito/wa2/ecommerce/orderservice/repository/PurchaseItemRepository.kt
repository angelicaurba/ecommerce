package it.polito.wa2.ecommerce.orderservice.repository
import it.polito.wa2.ecommerce.orderservice.domain.PurchaseItem
import it.polito.wa2.ecommerce.orderservice.it.polito.wa2.ecommerce.orderservice.domain.PurchaseItem
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface PurchaseItemRepository : PagingAndSortingRepository<PurchaseItem, Long>{

    @Modifying
    @Query("""update purchase_item 
            set warehouse_id = :warehouse_id 
            where order_id= :order_id and product_id = :product_id""")
    fun updateWarehouseByOrderAndProduct(@Value("order_id") orderId: Long,
                                         @Value("product_id") productId: String,
                                         @Value("warehouse_id") warehouseId: String)



}
