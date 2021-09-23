package it.polito.wa2.ecommerce.catalogueservice

import it.polito.wa2.ecommerce.catalogueservice.domain.Category
import it.polito.wa2.ecommerce.catalogueservice.domain.Comment
import it.polito.wa2.ecommerce.catalogueservice.domain.Photo
import it.polito.wa2.ecommerce.catalogueservice.domain.Product
import it.polito.wa2.ecommerce.catalogueservice.repository.CommentRepository
import it.polito.wa2.ecommerce.catalogueservice.repository.PhotoRepository
import it.polito.wa2.ecommerce.catalogueservice.repository.ProductRepository
import org.bson.types.Binary
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.context.annotation.Bean
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.math.BigDecimal
import java.net.URL
import java.util.*


@SpringBootApplication(
    scanBasePackages = [
        "it.polito.wa2.ecommerce.catalogueservice",
        "it.polito.wa2.ecommerce.common.security.utils",
        "it.polito.wa2.ecommerce.common.connection"
    ]
)
@EnableEurekaClient
@EnableReactiveMethodSecurity
@EnableWebFluxSecurity
@EnableReactiveMongoRepositories
class CatalogueServiceApplication {

    @Bean
    fun populateDB(
        productRepository: ProductRepository,
        commentRepository: CommentRepository,
        photoRepository: PhotoRepository
    )
            : CommandLineRunner {
        return CommandLineRunner {

            commentRepository.deleteAll()
            photoRepository.deleteAll()
            productRepository.deleteAll()

            val now = Date()
            val nowMinusOne = Date(now.time - 1000 * 60 * 60)
            val nowMinusTwo = Date(now.time - 1000 * 60 * 60 * 2)
            val nowMinusFive = Date(now.time - 1000 * 60 * 60 * 5)
            val yesterday = Date(now.time - 1000 * 60 * 60 * 24)

            val p1 = Product(
                "1",
                "Thinkpad L15",
                "personal computer from Lenovo",
                Category.TECH,
                BigDecimal("1200.51"),
                4,
                1,
                yesterday
            )

            val p2 = Product(
                "2",
                "Surface Pro 8",
                "2 in 1",
                Category.TECH,
                BigDecimal("2250.99"),
                0,
                0,
                nowMinusOne
            )

            val p3 = Product(
                "3",
                "A Room of One’s Own",
                "A Room of One’s Own - Virgina Woolf's book",
                Category.BOOKS,
                BigDecimal("12.20"),
                0,
                0,
                nowMinusTwo
            )

            val p4 = Product(
                "4",
                "Harry Potter and the Sorcerer's Stone",
                "First book of J. K. Rowling's saga",
                Category.BOOKS,
                BigDecimal("24.20"),
                9,
                2,
                nowMinusFive
            )

            productRepository.save(p1)
            productRepository.save(p2)
            productRepository.save(p3)
            productRepository.save(p4)

            val c1 = Comment(
                "1",
                "Amazing book!",
                "A great read at an affordable price. I highly recommend it",
                5,
                "4",
                "customer1"
            )

            val c2 = Comment(
                "2",
                "Not so bad",
                "It is not a bad read but frankly I prefer the movie",
                3,
                "4",
                "customer2"
            )

            val c3 = Comment(
                "3",
                "Great computer",
                "I use it to work in the office: good value for money and good performances. Well done!",
                5,
                "1",
                "customer1"
            )

            commentRepository.save(c1)
            commentRepository.save(c2)
            commentRepository.save(c3)

            val imageUrl = "https://i.pinimg.com/736x/1b/2b/fa/1b2bfa193ec0b2e72af49991ea0aff1a.jpg"
            val url = URL(imageUrl)
            val stream: InputStream = url.openStream()
            var byteArray = ByteArrayOutputStream()
            val b = ByteArray(2048)
            var length: Int
            while (stream.read(b).also { length = it } != -1) {
                byteArray.write(b, 0, length)
            }
            stream.close()

            val image = Binary(byteArray.toByteArray())

            val photo1 = Photo(
                "1",
                "jpg",
                image,
                "4"
            )

            photoRepository.save(photo1)

        }
    }
}

fun main(args: Array<String>) {
    runApplication<CatalogueServiceApplication>(*args)
}
