package com.fiapgrupo27.bucket.application.usecases;

import com.fiapgrupo27.bucket.application.gateways.BucketGateway;
import com.fiapgrupo27.bucket.domain.BucketFile;

public class UploadFileUseCase {
    private final BucketGateway bucketGateway;

    public UploadFileUseCase(BucketGateway bucketGateway) {
        this.bucketGateway = bucketGateway;
    }

    public void uploadArquivo(String filePath, String destinoS3) {
        String fileUrl = bucketGateway.uploadFile(filePath, destinoS3);

    }

}