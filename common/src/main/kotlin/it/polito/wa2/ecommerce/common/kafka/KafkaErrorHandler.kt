package it.polito.wa2.ecommerce.common.kafka

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.TopicPartition
import org.springframework.kafka.listener.ContainerAwareErrorHandler
import org.springframework.kafka.listener.MessageListenerContainer
import org.springframework.kafka.support.serializer.DeserializationException
import java.util.concurrent.atomic.AtomicBoolean
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.errors.SerializationException


internal class KafkaErrorHandler : ContainerAwareErrorHandler {
    override fun handle(
        thrownException: java.lang.Exception,
        records: MutableList<ConsumerRecord<*, *>>?,
        consumer: Consumer<*, *>,
        container: MessageListenerContainer
    ) {

        doSeeks(records!!, consumer)
        if (records.isNotEmpty()) {
            val record = records[0]
            val topic = record.topic()
            val offset = record.offset()
            val partition = record.partition()
            if (thrownException.javaClass == DeserializationException::class.java ||
                thrownException.javaClass == SerializationException::class.java) {
                val exception: DeserializationException = thrownException as DeserializationException
                val malformedMessage: String = String(exception.data)

               println(
                    "Skipping message with topic $topic and offset $offset " +
                            "- malformed message: $malformedMessage , exception: ${exception.getLocalizedMessage()}"
                )
            } else {
                println(
                    "Skipping message with topic $topic - offset $offset - partition $partition - exception ${thrownException.message}",
                )
            }
        } else {
            print("Consumer exception - cause: ${thrownException.message}")
        }
    }

    private fun doSeeks(records: List<ConsumerRecord<*, *>>, consumer: Consumer<*, *>) {
        val partitions: MutableMap<TopicPartition, Long> = LinkedHashMap()
        val first = AtomicBoolean(true)
        records.forEach{ record: ConsumerRecord<*, *> ->
            if (first.get()) {
                partitions[TopicPartition(record.topic(), record.partition())] = record.offset() + 1
            } else {
                partitions.computeIfAbsent(TopicPartition(record.topic(), record.partition()))
                    { record.offset() }
            }
            first.set(false)
        }
        partitions.forEach(consumer::seek)
    }




}