package io.pivotal.tsalm.s3.infrastructure.config

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3Client
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class InfrastructureConfiguration {

    @Value("\${s3Properties.endpointUrl}")
    private val endpointUrl: String? = null
    @Value("\${s3Properties.region}")
    private val region: String? = null
    @Value("\${s3Properties.accessKey}")
    private val accessKey: String? = null
    @Value("\${s3Properties.secretKey}")
    private val secretKey: String? = null

    @Bean
    fun amazonS3Client(): AmazonS3 {
        val credentialsProvider = AWSStaticCredentialsProvider(BasicAWSCredentials(accessKey, secretKey))
        return AmazonS3Client.builder()
                .withCredentials(credentialsProvider)
                .withEndpointConfiguration(EndpointConfiguration(endpointUrl, region))
                .build()
    }
}