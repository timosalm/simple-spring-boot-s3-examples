package io.pivotal.tsalm.s3.infrastructure

import com.amazonaws.services.s3.AmazonS3
import org.springframework.core.io.Resource
import org.springframework.core.io.ResourceLoader
import org.springframework.core.io.WritableResource
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class S3Service(private val resourceLoader: ResourceLoader, private val amazonS3: AmazonS3) {

    @Throws(S3KeyDoesNotExistException::class)
    fun getObject(bucketName: String, key: String): Resource {
        val resource = resourceLoader.getResource("s3://$bucketName/$key")
        if (resource.exists()) {
            return resource
        }
        throw S3KeyDoesNotExistException(bucketName, key)
    }

    fun putObject(bucketName: String, resource: Resource): String {
        val key = resource.filename ?: UUID.randomUUID().toString()
        val writableResource = this.resourceLoader.getResource("s3://$bucketName/$key") as WritableResource
        writableResource.outputStream.use { fileOut ->
            resource.inputStream.copyTo(fileOut)
        }
        return key
    }

    @Throws(S3KeyDoesNotExistException::class)
    fun removeObject(bucketName: String, key: String) {
        if (amazonS3.doesObjectExist(bucketName, key)) {
            amazonS3.deleteObject(bucketName, key)
        } else {
            throw S3KeyDoesNotExistException(bucketName, key)
        }
    }
}