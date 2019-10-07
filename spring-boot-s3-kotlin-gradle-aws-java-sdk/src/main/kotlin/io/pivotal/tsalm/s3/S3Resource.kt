package io.pivotal.tsalm.s3


import io.pivotal.tsalm.s3.infrastructure.S3KeyDoesNotExistException
import io.pivotal.tsalm.s3.infrastructure.S3Service
import org.springframework.core.io.Resource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartHttpServletRequest
import java.net.URI


@RestController
@RequestMapping(S3Resource.BASE_URI)
class S3Resource(private val service: S3Service) {

    companion object {
        const val BASE_URI = "/api/v1/buckets"
    }

    @GetMapping("/{bucketName}/objects/{objectKey}")
    fun downloadS3Object(@PathVariable bucketName: String, @PathVariable objectKey: String): ResponseEntity<Resource> {
        return try {
            val s3Object = service.getObject(bucketName, objectKey)
            ResponseEntity.ok(s3Object)
        } catch (exception: S3KeyDoesNotExistException) {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping("/{bucketName}/objects", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadS3Object(@PathVariable bucketName: String, request: MultipartHttpServletRequest): ResponseEntity<Void> {
        val multipartFile = request.multiFileMap.toSingleValueMap().values.first()
        val objectKey = service.putObject(bucketName, multipartFile.resource)
        return ResponseEntity.created(URI("$BASE_URI/$bucketName/objects/$objectKey")).build()
    }

    @DeleteMapping("/{bucketName}/objects/{objectKey}")
    fun removeObject(@PathVariable bucketName: String, @PathVariable objectKey: String): ResponseEntity<Void> {
        return try {
            service.removeObject(bucketName, objectKey)
            return ResponseEntity.noContent().build()
        } catch (exception: S3KeyDoesNotExistException) {
            ResponseEntity.notFound().build()
        }
    }
}