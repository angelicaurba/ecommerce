package it.polito.wa2.ecommerce.common.saga.service

import it.polito.wa2.ecommerce.common.saga.domain.Emittable

interface MessageService {

    fun publish(message: Emittable, messageType:String, topic:String, persistInDb: Boolean = false)
}