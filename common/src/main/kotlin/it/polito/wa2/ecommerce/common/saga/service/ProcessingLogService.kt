package it.polito.wa2.ecommerce.common.saga.service

import it.polito.wa2.ecommerce.common.saga.domain.ProcessedEvent
import it.polito.wa2.ecommerce.common.saga.repository.ProcessedEventRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
class ProcessingLogService {

    @Autowired
    lateinit var processedEventRepository: ProcessedEventRepository

    fun isProcessed(eventID: UUID) = processedEventRepository.findByIdOrNull(eventID) == null

    fun process(eventID: UUID) = processedEventRepository.save(ProcessedEvent(eventID))
}