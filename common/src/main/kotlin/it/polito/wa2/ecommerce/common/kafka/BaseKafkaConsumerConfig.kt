package it.polito.wa2.ecommerce.common.kafka

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.kafka.annotation.KafkaListenerConfigurer
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.config.KafkaListenerEndpointRegistrar
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean

abstract class BaseKafkaConsumerConfig<T>(private val myClass: Class<T>) : KafkaListenerConfigurer {

    @Value("\${spring.kafka.bootstrap-servers}")
    lateinit var bootstrapServers:String

    @Value("\${spring.kafka.consumer.group-id}")
    lateinit var groupId:String

    @Autowired
    lateinit var validator: LocalValidatorFactoryBean

    private fun consumerFactory(): ConsumerFactory<String, T> {
        val props: MutableMap<String, Any> = HashMap()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
        props[ConsumerConfig.GROUP_ID_CONFIG] = groupId
        props[ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG] = true
        props[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "latest"

        val headerErrorHandlingDeserializer: ErrorHandlingDeserializer<String> = ErrorHandlingDeserializer(
            StringDeserializer()
        )
        val errorHandlingDeserializer: ErrorHandlingDeserializer<T> = ErrorHandlingDeserializer(
            JsonDeserializer(
                myClass
            )
        )
        return DefaultKafkaConsumerFactory(
            props,
            headerErrorHandlingDeserializer,
            errorHandlingDeserializer
        )
    }

    @Bean
    fun concurrentKafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, T>? {
        val factory: ConcurrentKafkaListenerContainerFactory<String, T> =
            ConcurrentKafkaListenerContainerFactory<String, T>()
        factory.consumerFactory = consumerFactory()
        factory.setErrorHandler(KafkaErrorHandler())
        return factory
    }

    override fun configureKafkaListeners(registrar: KafkaListenerEndpointRegistrar) {
        registrar.setValidator(validator)
    }
}