package io.pivotal.tsalm.s3.infrastructure;

public class S3KeyDoesNotExistException extends RuntimeException {
    S3KeyDoesNotExistException(String bucketName, String key) {
        super(String.format("The key %s does not exist in bucket %s", bucketName, key));
    }
}
