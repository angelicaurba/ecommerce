/*
package it.polito.wa2.ecommerce.mailservice.consumer


import it.polito.wa2.ecommerce.mailservice.client.MailDTO
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.serializer.JsonDeserializer


// From https://www.baeldung.com/spring-kafka
// Section 5.2: "Consuming Messages"
// Section 6.2: "Consuming Custom Messages"

@EnableKafka
@Configuration
class KafkaConsumerConfig {

    @Bean
    fun consumerFactory(): ConsumerFactory<String, MailDTO>{
        val props: MutableMap<String, Any> = HashMap()
        // TODO define bootstrapAddress and groupID
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress
        props[ConsumerConfig.GROUP_ID_CONFIG] = groupId
        props[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        props[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
        return DefaultKafkaConsumerFactory<String, MailDTO>(
            props,
            StringDeserializer(),
            JsonDeserializer(MailDTO::class.java)
        )
    }

    @Bean
    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, MailDTO>? {
        val factory: ConcurrentKafkaListenerContainerFactory<String, MailDTO> =
            ConcurrentKafkaListenerContainerFactory<String, MailDTO>()
        factory.setConsumerFactory(consumerFactory())
        return factory
    }
}
*/