package com.david.servidor

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Servidor

fun main(args: Array<String>) {
	runApplication<Servidor>(*args)
}