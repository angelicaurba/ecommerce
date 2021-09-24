package it.polito.wa2.ecommerce.orderservice.repository
import it.polito.wa2.ecommerce.orderservice.domain.Order
import it.polito.wa2.ecommerce.orderservice.domain.PurchaseItem
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface PurchaseItemRepository : PagingAndSortingRepository<PurchaseItem, Long> {

    @Modifying
    @Query("""update PurchaseItem
            set warehouseId = :warehouse_id
            where order= :order and productId = :product_id""")
    fun setWarehouseByOrderAndProduct(@Param("order") order: Order,
                                      @Param("product_id") productId: String,
                                      @Param("warehouse_id") warehouseId: String)


    @Transactional(readOnly = true)
    @Query("""
        select case when count(PurchaseItem) > 0 then true else false end from PurchaseItem
        where productId = :product_id and order.buyerId = :buyer_id
    """)
    fun haveBuyerIdBoughtProduct(
        @Param("product_id") productId: String,
        @Param("buyer_id") buyerId: String
    ): Boolean

}
