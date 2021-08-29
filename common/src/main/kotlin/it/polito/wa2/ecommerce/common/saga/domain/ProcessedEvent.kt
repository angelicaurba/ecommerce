package it.polito.wa2.ecommerce.common.saga.domain

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class ProcessedEvent(
    @Id
    var eventId: UUID
)