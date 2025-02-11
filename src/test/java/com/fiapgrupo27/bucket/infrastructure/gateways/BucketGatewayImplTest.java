package com.fiapgrupo27.bucket.infrastructure.gateways;

import com.fiapgrupo27.bucket.infrastructure.gateways.BucketGatewayImpl;
import com.fiapgrupo27.bucket.infrastructure.adapters.S3Adapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class BucketGatewayImplTest {

    @Mock
    private S3Adapter s3Adapter;  // Mock do S3Adapter

    private BucketGatewayImpl bucketGateway;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Inicializa os mocks
        bucketGateway = new BucketGatewayImpl("my-bucket", s3Adapter);  // Cria a instância da classe com o mock
    }

    @Test
    void testUploadFile() {
        // Configura o comportamento do mock
        String filePath = "path/to/file.txt";
        String fileName = "file.txt";
        String expectedResult = "upload-success";

        when(s3Adapter.uploadFile(filePath, fileName, "my-bucket")).thenReturn(expectedResult);

        // Chama o método a ser testado
        String result = bucketGateway.uploadFile(filePath, fileName);

        // Verifica se o resultado é o esperado
        assertEquals(expectedResult, result);

        // Verifica se o método uploadFile foi chamado uma vez com os parâmetros corretos
        verify(s3Adapter, times(1)).uploadFile(filePath, fileName, "my-bucket");
    }

    @Test
    void testDownloadFile() {
        // Configura o comportamento do mock
        String fileName = "file.txt";
        Path expectedPath = Paths.get("path/to/downloaded/file.txt");

        when(s3Adapter.downloadFile(fileName)).thenReturn(expectedPath);

        // Chama o método a ser testado
        Path result = bucketGateway.downloadFile(fileName);

        // Verifica se o caminho retornado é o esperado
        assertEquals(expectedPath, result);

        // Verifica se o método downloadFile foi chamado uma vez com o nome do arquivo correto
        verify(s3Adapter, times(1)).downloadFile(fileName);
    }
}
