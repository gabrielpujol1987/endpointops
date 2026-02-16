package com.gabriel.endpointops

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan
@SpringBootApplication
class EndpointopsApplication

fun main(args: Array<String>) {
    runApplication<EndpointopsApplication>(*args)
}
