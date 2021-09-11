package it.polito.wa2.ecommerce.common.connection

import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class WebClientBuilder {
    @Bean
    @LoadBalanced
    fun getWebClientBuilder(): WebClient.Builder{
        return WebClient.builder()
    }

}