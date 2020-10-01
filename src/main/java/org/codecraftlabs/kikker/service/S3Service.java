package org.codecraftlabs.kikker.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import javax.annotation.Nonnull;
import java.io.File;

import static software.amazon.awssdk.core.sync.RequestBody.fromFile;

public class S3Service {
    private static final Logger logger = LogManager.getLogger(S3Service.class);
    private S3Client s3Client;

    public S3Service(@Nonnull final Region region) {
        s3Client = S3Client.builder().region(region).build();
    }

    public void upload(@Nonnull String bucket, @Nonnull String key, @Nonnull String file) throws AWSException {
        try {
            s3Client.putObject(PutObjectRequest.builder().bucket(bucket).key(key).build(), fromFile(new File(file)));
        } catch (AwsServiceException exception) {
            String errorMessage = "Error when uploading object";
            logger.warn(errorMessage, exception);
            throw new AWSException(errorMessage, exception);
        }
    }
}
