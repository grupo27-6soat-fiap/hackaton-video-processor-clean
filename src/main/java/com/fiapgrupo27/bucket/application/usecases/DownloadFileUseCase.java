package com.fiapgrupo27.bucket.application.usecases;

import com.fiapgrupo27.bucket.application.gateways.BucketGateway;

import java.nio.file.Path;

public class DownloadFileUseCase {
    private final BucketGateway bucketGateway;

    public DownloadFileUseCase(BucketGateway bucketGateway) {
        this.bucketGateway = bucketGateway;
    }
    public Path downloadFile(String nomeArquivo) {
        Path arquivoBaixado = bucketGateway.downloadFile(nomeArquivo);
        return arquivoBaixado;
    }
}
