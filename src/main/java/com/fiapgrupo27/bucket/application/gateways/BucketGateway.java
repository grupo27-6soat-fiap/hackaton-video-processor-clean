package com.fiapgrupo27.bucket.application.gateways;

import java.nio.file.Path;

public interface BucketGateway {
    String uploadFile(String filePath, String destinoS3);
    Path downloadFile(String nomeArquivo);
}