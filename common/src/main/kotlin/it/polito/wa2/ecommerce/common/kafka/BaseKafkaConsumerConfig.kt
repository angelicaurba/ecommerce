package it.polito.wa2.ecommerce.common.kafka

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.serializer.JsonDeserializer

abstract class BaseKafkaConsumerConfig<T>(private val myClass: Class<T>){

    @Value("\${spring.kafka.bootstrap-servers}")
    lateinit var bootstrapServers:String

    @Value("\${spring.kafka.consumer.group-id}")
    lateinit var groupId:String

    @Bean
    fun consumerFactory(): ConsumerFactory<String, T> {
        val props: MutableMap<String, Any> = HashMap()
        // TODO define bootstrapAddress and groupID
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
        props[ConsumerConfig.GROUP_ID_CONFIG] = groupId
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = JsonDeserializer::class.java
        props[JsonDeserializer.VALUE_DEFAULT_TYPE] = myClass
        return DefaultKafkaConsumerFactory(props)
    }

    @Bean
    fun orderStatusFactory(consumerFactory: ConsumerFactory<String, T>): ConcurrentKafkaListenerContainerFactory<String, T>? {
        val factory: ConcurrentKafkaListenerContainerFactory<String, T> =
            ConcurrentKafkaListenerContainerFactory<String, T>()
        factory.consumerFactory = consumerFactory
        return factory
    }
}