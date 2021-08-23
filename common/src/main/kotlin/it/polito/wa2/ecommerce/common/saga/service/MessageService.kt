package it.polito.wa2.ecommerce.common.saga.service

import com.fasterxml.jackson.databind.ObjectMapper
import it.polito.wa2.ecommerce.common.saga.domain.Emittable
import it.polito.wa2.ecommerce.common.saga.domain.OutboxEvent
import it.polito.wa2.ecommerce.common.saga.repository.OutboxRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MessageService {

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var outboxRepository: OutboxRepository

    fun publish(message:Emittable, messageType:String, topic:String, persistInDb: Boolean = false){
        val outboxEvent = OutboxEvent(
            type = messageType ,
            destinationTopic = topic,
            payload = objectMapper.writeValueAsString(message),
            payloadId = message.getId()
        )

        val persistedEvent = outboxRepository.save(outboxEvent)
        if(!persistInDb)
            outboxRepository.delete(persistedEvent)

    }
}