package io.pivotal.tsalm.s3.infrastructure;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.util.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.WritableResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

@Service
public class S3Service {

    private ResourceLoader resourceLoader;
    private AmazonS3 amazonS3;

    public S3Service(ResourceLoader resourceLoader, AmazonS3 amazonS3) {
        this.resourceLoader = resourceLoader;
        this.amazonS3 = amazonS3;
    }

    public Resource getObject(String bucketName, String key) throws S3KeyDoesNotExistException {
        final Resource resource = resourceLoader.getResource(String.format("s3://%s/%s", bucketName, key));
        if (resource.exists()) {
            return resource;
        }
        throw new S3KeyDoesNotExistException(bucketName, key);
    }

    public String putObject(String bucketName, Resource resource) throws IOException {
        String key = resource.getFilename() != null ? resource.getFilename() : UUID.randomUUID().toString();
        final WritableResource writableResource = (WritableResource) this.resourceLoader
                .getResource(String.format("s3://%s/%s", bucketName, key));

        try (OutputStream outputStream = writableResource.getOutputStream()) {
            IOUtils.copy(resource.getInputStream(), outputStream);
        }

        return key;
    }

    public void removeObject(String bucketName, String key) throws S3KeyDoesNotExistException {
        if (amazonS3.doesObjectExist(bucketName, key)) {
            amazonS3.deleteObject(bucketName, key);
        } else {
            throw new S3KeyDoesNotExistException(bucketName, key);
        }
    }
}