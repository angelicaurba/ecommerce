package it.polito.wa2.ecommerce.warehouseservice.repository

import it.polito.wa2.ecommerce.warehouseservice.domain.Warehouse
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface WarehouseRepository : PagingAndSortingRepository<Warehouse,Long>{
    @Query(value = "update warehouse set id = :newID where id = :oldID", nativeQuery = true)
    @Transactional(readOnly = false)
    @Modifying(clearAutomatically = true)
    fun updateID(
        @Param("newID") newID: Long,
        @Param("oldID") oldID: Long
    )
}