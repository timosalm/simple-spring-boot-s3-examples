package io.pivotal.tsalm.s3.infrastructure

import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.Resource
import org.springframework.stereotype.Repository
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.*
import java.util.*

@Repository
class S3Service(private val s3Client: S3Client) {

    @Throws(S3KeyDoesNotExistException::class)
    fun getObject(bucketName: String, key: String): Resource {
        val request = GetObjectRequest.builder().bucket(bucketName).key(key).build()
        try {
            val responseInputStream = s3Client.getObject(request)
            return InputStreamResource(responseInputStream)
        } catch (exception: NoSuchKeyException) {
            throw S3KeyDoesNotExistException(bucketName, key)
        }
    }

    fun putObject(bucketName: String, resource: Resource): String {
        val key = resource.filename ?: UUID.randomUUID().toString()
        val request = PutObjectRequest.builder().bucket(bucketName).key(key).build()
        val requestBody = RequestBody.fromInputStream(resource.inputStream, resource.contentLength())
        s3Client.putObject(request, requestBody)
        return key
    }

    @Throws(S3KeyDoesNotExistException::class)
    fun removeObject(bucketName: String, key: String) {
        try {
            s3Client.headObject(HeadObjectRequest.builder().bucket(bucketName).key(key).build())
        } catch (exception: NoSuchKeyException) {
            throw S3KeyDoesNotExistException(bucketName, key)
        }
        s3Client.deleteObject(DeleteObjectRequest.builder().bucket(bucketName).key(key).build())
    }
}