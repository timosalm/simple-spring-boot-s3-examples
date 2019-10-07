package io.pivotal.tsalm.s3.infrastructure.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@ConfigurationProperties("s3properties")
@Component
class S3ConfigurationProperties {

    lateinit var endpointUrl: String
    lateinit var region: String
    lateinit var accessKey: String
    lateinit var secretKey: String
}
