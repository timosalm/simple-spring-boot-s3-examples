package io.pivotal.tsalm.s3.infrastructure;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3Service {

    private S3Client s3Client;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    public Resource getObject(String bucketName, String key) throws S3KeyDoesNotExistException {
        GetObjectRequest request = GetObjectRequest.builder().bucket(bucketName).key(key).build();
        try {
            ResponseInputStream<GetObjectResponse> responseInputStream = s3Client.getObject(request);
            return new InputStreamResource(responseInputStream);
        } catch (NoSuchKeyException exception) {
            throw new S3KeyDoesNotExistException(bucketName, key);
        }
    }

    public String putObject(String bucketName, Resource resource) throws IOException {
        String key = resource.getFilename() != null ? resource.getFilename() : UUID.randomUUID().toString();
        PutObjectRequest request = PutObjectRequest.builder().bucket(bucketName).key(key).build();
        RequestBody requestBody = RequestBody.fromInputStream(resource.getInputStream(), resource.contentLength());
        s3Client.putObject(request, requestBody);
        return key;
    }

    public void removeObject(String bucketName, String key) {
        try {
            s3Client.headObject(HeadObjectRequest.builder().bucket(bucketName).key(key).build());
        } catch (NoSuchKeyException exception) {
            throw new S3KeyDoesNotExistException(bucketName, key);
        }
        s3Client.deleteObject(DeleteObjectRequest.builder().bucket(bucketName).key(key).build());
    }
}