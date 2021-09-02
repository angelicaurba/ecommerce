package it.polito.wa2.ecommerce.mailservice.consumer

import it.polito.wa2.ecommerce.common.kafka.BaseKafkaConsumerConfig
import it.polito.wa2.ecommerce.mailservice.client.MailDTO
import org.springframework.stereotype.Component

@Component
class MailConsumerConfig: BaseKafkaConsumerConfig<MailDTO>(MailDTO::class.java)