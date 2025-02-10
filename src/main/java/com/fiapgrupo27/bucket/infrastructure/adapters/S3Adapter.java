package com.fiapgrupo27.bucket.infrastructure.adapters;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.Files;

@Component
public class S3Adapter {
    private final S3Client s3Client;
    private final String bucketName;

    public S3Adapter(S3Client s3Client, @Value("${aws.s3.bucket-name}") String bucketName) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

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
            throw new RuntimeException("Erro ao tentar baixar o arquivo", e);
        }

        if (!Files.exists(tempFile)) {
            throw new RuntimeException("Arquivo de vídeo não encontrado: " + nomeArquivo);
        }


        return tempFile;
    }

    public String uploadFile(String filePath, String fileName, String bucketName) {
        Path path = Path.of(filePath);
        File file = path.toFile();
        if (!file.exists()) {
            throw new RuntimeException("Arquivo não encontrado para upload: " + path.getFileName());
        }

        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        s3Client.putObject(putRequest, path);
        System.out.println("Upload concluído para S3: " + bucketName + "/" + fileName);
        return filePath;
    }
}
