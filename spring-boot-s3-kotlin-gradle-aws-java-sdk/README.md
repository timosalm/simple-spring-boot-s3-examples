Simple S3 sample using AWS Java SDK
============

This is a example application for using S3 compatible storage with [Spring Boot](http://projects.spring.io/spring-boot/),
[AWS Java SDK](https://aws.amazon.com/sdk-for-java/), Kotlin and Gradle.

The compatibility was tested with Google Cloud Storage. See https://cloud.google.com/storage/docs/migrating#migration-simple 

## Running the application locally
1. Clone the repository and move to the project folder.
    ```
    $ git clone https://github.com/tsalm-pivotal/simple-spring-boot-s3-examples
    $ cd simple-spring-boot-s3-examples/spring-boot-s3-kotlin-gradle-aws-java-sdk
    ```
2. Open the application.yml file in src/main/resources and replace the placeholder values with those appropriate for your environment.
    ```
    s3Properties:
      endpointUrl: https://s3.eu-central-1.amazonaws.com
      region: eu-central-1
      accessKey: YOUR_ACCESS_KEY
      secretKey: YOUR_SECRET_KEY
    ```
3. Run the application.
    ```
    $ ./gradlew bootRun
    ```
4. Call the API.
    - Upload a file to an existing bucket in the configured region:
        ```
            $curl -F "data=@/YOUR/PATH/TO/FILE.txt" http://localhost:8080/api/v1/buckets/YOUR_BUCKET_NAME/objects -v
        ```
    - Download an existing file from a bucket in the configured region:
        ```
            $curl -O http://localhost:8080/api/v1/buckets/YOUR_BUCKET_NAME/objects/FILE.txt -v
        ```
    - Delete an existing file from a bucket in the configured region:
        ```
            $curl -X "DELETE" http://localhost:8080/api/v1/buckets/YOUR_BUCKET_NAME/objects/FILE.txt -v
        ```