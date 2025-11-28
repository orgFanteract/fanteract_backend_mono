package org.fanteract

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FanteractApplication

fun main(args: Array<String>) {
	runApplication<FanteractApplication>(*args)
}
