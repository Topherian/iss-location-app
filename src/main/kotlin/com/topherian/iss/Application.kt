package com.topherian.iss

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients(basePackages = ["com.topherian.iss.external.feign"])
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
