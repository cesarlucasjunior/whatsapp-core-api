package com.cesarlucasjunior.whatsappcoreapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class WhatsappCoreApiApplication

fun main(args: Array<String>) {
	runApplication<WhatsappCoreApiApplication>(*args)
}
