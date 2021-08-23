package it.polito.wa2.ecommerce.common.saga.repository

import it.polito.wa2.ecommerce.common.saga.domain.ProcessedEvent
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProcessedEventRepository: CrudRepository<ProcessedEvent, UUID>{
}