package com.fiapgrupo27.bucket.application.usecases;

import com.fiapgrupo27.bucket.application.gateways.BucketGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class UploadFileUseCaseTest {

    @Mock
    private BucketGateway bucketGateway;

    @InjectMocks
    private UploadFileUseCase uploadFileUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUploadArquivo() {
        String filePath = "caminho/do/arquivo.txt";
        String destinoS3 = "bucket/destino";
        
        when(bucketGateway.uploadFile(filePath, destinoS3)).thenReturn("https://s3.amazonaws.com/bucket/destino");
        
        uploadFileUseCase.uploadArquivo(filePath, destinoS3);
        
        verify(bucketGateway, times(1)).uploadFile(filePath, destinoS3);
    }
}
