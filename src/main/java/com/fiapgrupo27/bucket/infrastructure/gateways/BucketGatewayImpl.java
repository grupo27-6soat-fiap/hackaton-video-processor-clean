package com.fiapgrupo27.bucket.infrastructure.gateways;

import com.fiapgrupo27.bucket.application.gateways.BucketGateway;
import com.fiapgrupo27.bucket.infrastructure.adapters.S3Adapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Component
public class BucketGatewayImpl implements BucketGateway {
    private final String bucketName;
    private final S3Adapter s3Adapter;

    public BucketGatewayImpl(@Value("${cloud.aws.s3.bucket}")String bucketName, S3Adapter s3Adapter) {
        this.bucketName = System.getenv().getOrDefault("AWS_S3_BUCKET_NAME", bucketName);
        this.s3Adapter = s3Adapter;


    }

    @Override
    public String uploadFile(String filePath, String fileName) {
        return s3Adapter.uploadFile(filePath, fileName, bucketName);


    }

    @Override
    public Path downloadFile(String nomeArquivo) {
        return s3Adapter.downloadFile(nomeArquivo);

    }
}