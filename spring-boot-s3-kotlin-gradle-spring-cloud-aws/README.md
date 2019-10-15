Simple S3 sample using Spring Cloud AWS
============

This is a example application for using AWS S3 with [Spring Boot](http://projects.spring.io/spring-boot/),
[Spring Cloud AWS](https://cloud.spring.io/spring-cloud-aws/reference/html/), Kotlin and Gradle.

*Hint: The configuration of the endpoint for the AWS S3 client is currently not supported by 
[Spring Cloud AWS](https://cloud.spring.io/spring-cloud-aws/reference/html/). 
So S3 compatible Storage is not supported!
See https://github.com/spring-cloud/spring-cloud-aws/issues/333*

## Running the application locally
1. Clone the repository and move to the project folder.
    ```
    $ git clone https://github.com/tsalm-pivotal/simple-spring-boot-s3-examples
    $ cd simple-spring-boot-s3-examples/spring-boot-s3-kotlin-gradle-spring-cloud-aws
    ```
2. Open the application.yml file in src/main/resources and replace the placeholder values with those appropriate for your environment.
    ```
    cloud.aws:
      credentials:
        accessKey: YOUR_ACCESS_KEY
        secretKey: YOUR_SECRET_KEY
      region:
        static: eu-central-1
      stack:
        auto: false
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

## Running the application on Cloud Foundry
1. Clone the repository and move to the project folder.
    ```
    $ git clone https://github.com/tsalm-pivotal/simple-spring-boot-s3-examples
    $ cd simple-spring-boot-s3-examples/spring-boot-s3-kotlin-gradle-spring-cloud-aws
    ```
2. Open the manifest.yml file and replace the placeholder values with those appropriate for your environment.
    ```
    ...
    CLOUD_AWS_CREDENTIALS_ACCESSKEY: YOUR_ACCESS_KEY
    CLOUD_AWS_CREDENTIALS_SECRETKEY: YOUR_SECRET_KEY
    CLOUD_AWS_REGION_STATIC: eu-central-1
    CLOUD_AWS_STACK_AUTO: false
    ```
3. After installing the 'cf' [command-line interface for Cloud Foundry](http://docs.cloudfoundry.org/cf-cli/), targeting a Cloud Foundry instance, and logging in, the application can be built and pushed using these commands:

    ```
    $ ./gradlew bootJar
    $ cf push
    ```
4. Call the API.
    - Upload a file to an existing bucket in the configured region:
        ```
            $curl -F "data=@/YOUR/PATH/TO/FILE.txt"  https://YOUR-APP-URL/api/v1/buckets/YOUR_BUCKET_NAME/objects -v
        ```
    - Download an existing file from a bucket in the configured region:
        ```
            $curl -O https://YOUR-APP-URL/api/v1/buckets/YOUR_BUCKET_NAME/objects/FILE.txt -v
        ```
    - Delete an existing file from a bucket in the configured region:
        ```
            $curl -X "DELETE" https://YOUR-APP-URL/api/v1/buckets/YOUR_BUCKET_NAME/objects/FILE.txt -v
        ```