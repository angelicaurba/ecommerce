package it.polito.wa2.ecommerce.common

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.*
import org.springframework.http.HttpStatus
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class ErrorMessageDTO( exception: Exception? = null,
                       httpStatus : HttpStatus ? = null,
                       var path: String = "",
){
    var timestamp: String = DateTimeFormatter
        .ofPattern("yyyy-MM-dd HH:mm:ss")
        .withZone(ZoneOffset.systemDefault())
        .format(Instant.now())
    var error: String = httpStatus?.name?.toLowerCase()?.capitalize() ?: "Generic error"
    var message: String = exception?.message ?: httpStatus?.reasonPhrase ?: "Generic error"
    var status: Int = httpStatus?.value() ?: 500

}

object ErrorMessageDTOSerializer: KSerializer<ErrorMessageDTO>{
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("ErrorMessageDTO") {
        element<String>("timestamp")
        element<Int>("status")
        element<String>("error")
        element<String>("message")
        element<String>("path")
    }

    override fun deserialize(decoder: Decoder): ErrorMessageDTO {
        return decoder.decodeStructure(descriptor) {
            val error = ErrorMessageDTO()
            while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    0-> error.timestamp = decodeStringElement(descriptor, 0)
                    1 -> error.status = decodeIntElement(descriptor, 1)
                    2 -> error.error = decodeStringElement(descriptor, 2)
                    3 -> error.message = decodeStringElement(descriptor, 3)
                    4 -> error.path = decodeStringElement(descriptor, 4)
                    CompositeDecoder.DECODE_DONE -> break
                    else -> error("Unexpected index: $index")
                }
            }
            error
        }
    }

    override fun serialize(encoder: Encoder, value: ErrorMessageDTO) {
        encoder.encodeStructure(descriptor) {
            encodeStringElement(descriptor, 0, value.timestamp)
            encodeIntElement(descriptor, 1, value.status)
            encodeStringElement(descriptor, 2, value.error)
            encodeStringElement(descriptor, 3, value.message)
            encodeStringElement(descriptor, 4, value.path)

        }
    }

}

