package io.pivotal.tsalm.s3.infrastructure.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import java.net.URI

@Configuration
class InfrastructureConfiguration(private val s3ConfigurationProperties: S3ConfigurationProperties) {

    @Bean
    fun s3Client(): S3Client {
        val credentials = AwsBasicCredentials.create(s3ConfigurationProperties.accessKey,
                s3ConfigurationProperties.secretKey)
        val credentialsProvider = StaticCredentialsProvider.create(credentials)
        return S3Client.builder()
                .credentialsProvider(credentialsProvider)
                .endpointOverride(URI.create(s3ConfigurationProperties.endpointUrl))
                .region(Region.of(s3ConfigurationProperties.region))
                .build()
    }
}