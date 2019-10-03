package io.pivotal.tsalm.s3.infrastructure

import com.amazonaws.services.s3.AmazonS3
import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.Resource
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class S3Repository(private val s3Client: AmazonS3) {

    @Throws(S3KeyDoesNotExistException::class)
    fun getObject(bucketName: String, key: String): InputStreamResource {
        if (s3Client.doesObjectExist(bucketName, key)) {
            val bucketObject = s3Client.getObject(bucketName, key)
            return InputStreamResource(bucketObject.objectContent)
        }
        throw S3KeyDoesNotExistException(bucketName, key)
    }

    fun putObject(bucketName: String, resource: Resource): String {
        val key = resource.filename ?: UUID.randomUUID().toString()
        s3Client.putObject(bucketName, key, resource.inputStream, null)
        return key
    }

    @Throws(S3KeyDoesNotExistException::class)
    fun removeObject(bucketName: String, key: String) {
        if (s3Client.doesObjectExist(bucketName, key)) {
            s3Client.deleteObject(bucketName, key)
        } else {
            throw S3KeyDoesNotExistException(bucketName, key)
        }
    }
}