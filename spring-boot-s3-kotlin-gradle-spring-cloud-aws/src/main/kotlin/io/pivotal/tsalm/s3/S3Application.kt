package io.pivotal.tsalm.s3

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class S3Application

fun main(args: Array<String>) {
	runApplication<S3Application>(*args)
}
