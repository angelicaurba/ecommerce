package it.polito.wa2.ecommerce.warehouseservice.config

import it.polito.wa2.ecommerce.common.kafka.BaseKafkaConsumerConfig
import it.polito.wa2.ecommerce.warehouseservice.client.order.request.WarehouseOrderRequestDTO
import org.springframework.stereotype.Component

@Component
class OrderRequestConsumerConfig: BaseKafkaConsumerConfig<WarehouseOrderRequestDTO>(WarehouseOrderRequestDTO::class.java)
