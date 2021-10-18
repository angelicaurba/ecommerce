package it.polito.wa2.ecommerce.common.saga.domain

import it.polito.wa2.ecommerce.common.EntityBase
import java.time.Instant
import java.time.Instant.now
import java.util.*
import java.util.UUID.randomUUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "outbox")
//@TypeDef(name = "jsonb", typeClass = JsonBinaryType::class)
class OutboxEvent(
    val timestamp: Instant = now(),

    @Column(name = "aggregate_id", nullable = false)
    val payloadId: String, //will contain the ID of the payload (emitted as key of the mesage)
    @Column(name = "destination_topic", nullable = false)
    val destinationTopic: String,
    @Column(name = "payload", nullable = false, columnDefinition = "json")
    val payload: String,
    val type: String
): EntityBase<Long>(){
    @Column(name="event_id", nullable = false)
    val eventId: String = randomUUID().toString()
}