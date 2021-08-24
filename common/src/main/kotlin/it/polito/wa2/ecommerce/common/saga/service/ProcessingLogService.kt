package it.polito.wa2.ecommerce.common.saga.service

import java.util.*

interface ProcessingLogService  {

    fun isProcessed(eventID: UUID): Boolean

    fun process(eventID: UUID)

}
