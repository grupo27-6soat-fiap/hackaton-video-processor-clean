package com.fiapgrupo27.bucket.infrastructure.gateways;

import com.fiapgrupo27.bucket.application.gateways.BucketGateway;
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
    private final S3Client s3Client;

    public BucketGatewayImpl(S3Client s3Client, @Value("${cloud.aws.s3.bucket}")String bucketName) {
        this.bucketName = bucketName;
        this.s3Client = s3Client;

    }

    @Override
    public String uploadFile(String filePath, String fileName) {
        Path path = Path.of(filePath);
        File file = path.toFile();

        if (!file.exists()) {
            throw new RuntimeException("Arquivo não encontrado para upload: " + filePath);
        }

        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        s3Client.putObject(putRequest, path);
        System.out.println("Upload concluído para S3: " + bucketName + "/" + fileName);
        return filePath;
    }

    @Override
    public Path downloadFile(String nomeArquivo) {
        String extensao = nomeArquivo.contains(".") ? nomeArquivo.substring(nomeArquivo.lastIndexOf(".")) : ".tmp";
        Path tempFile = null;
        try {
            tempFile = Files.createTempFile("video_", extensao);
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(nomeArquivo)
                    .build();
            var s3Object = s3Client.getObject(getObjectRequest);
            var inputStream = s3Object;
            Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (!Files.exists(tempFile)) {
            throw new RuntimeException("Arquivo de vídeo não encontrado: " + nomeArquivo);
        }


        return tempFile;
    }
}