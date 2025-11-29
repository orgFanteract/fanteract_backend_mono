package org.fanteract

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class FanteractApplication

fun main(args: Array<String>) {
	runApplication<FanteractApplication>(*args)
}
