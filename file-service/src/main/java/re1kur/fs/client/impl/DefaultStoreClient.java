package re1kur.fs.client.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import re1kur.core.other.PresignedUrl;
import re1kur.fs.client.StoreClient;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultStoreClient implements StoreClient {
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Value("${minio.default-bucket}")
    private String bucket;

    @Value("${minio.url.time-to-live}")
    private Integer ttl;

    @PostConstruct
    public void init() {
        try {
            HeadBucketRequest headBucketRequest = HeadBucketRequest.builder()
                    .bucket(bucket)
                    .build();
            s3Client.headBucket(headBucketRequest);
            log.info("Bucket '{}' already exists", bucket);
        } catch (S3Exception e) {
            if (e instanceof NoSuchBucketException || e.statusCode() == 404) {
                CreateBucketRequest createBucketRequest = CreateBucketRequest.builder()
                        .bucket(bucket)
                        .build();
                CreateBucketResponse response = s3Client.createBucket(createBucketRequest);
                log.info("Created bucket: {}", response.location());
            } else {
                log.error("Error checking/creating bucket: {}", e.awsErrorDetails().errorMessage(), e);
                throw e;
            }
        }
    }


    @Override
    public void upload(String id, MultipartFile payload) throws IOException {
        PutObjectRequest request = PutObjectRequest.builder()
                .key(id)
                .bucket(bucket)
                .build();
        s3Client.putObject(request, RequestBody.fromInputStream(payload.getInputStream(), payload.getSize()));
    }

    @Override
    public PresignedUrl getUrl(String id) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(id)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofDays(ttl))
                .getObjectRequest(getObjectRequest)
                .build();

        PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(presignRequest);
        return new PresignedUrl(presignedGetObjectRequest.url().toExternalForm(), presignedGetObjectRequest.expiration());
    }
}
