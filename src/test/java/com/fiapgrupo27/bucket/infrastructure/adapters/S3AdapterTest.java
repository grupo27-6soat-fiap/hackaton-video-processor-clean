package com.fiapgrupo27.bucket.infrastructure.adapters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class S3AdapterTest {

    private S3Adapter s3Adapter;
    private S3Client s3Client;

    @BeforeEach
    public void setUp() {
        s3Client = mock(S3Client.class); // Mock do S3Client
        s3Adapter = new S3Adapter(s3Client, "test-bucket");
    }

    @Test
    public void testDownloadFile_ArquivoNaoExistente() {
        // Configura o mock para simular a falha no download
        when(s3Client.getObject(any(GetObjectRequest.class)))
            .thenThrow(new RuntimeException("Arquivo de vídeo não encontrado: nonexistent-file.mp4"));

        // Testar o comportamento quando o arquivo não existe
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            s3Adapter.downloadFile("nonexistent-file.mp4");
        });

        // Verificar se a mensagem da exceção é a esperada
        assertEquals("Arquivo de vídeo não encontrado: nonexistent-file.mp4", exception.getMessage());
    }

    @Test
    public void testUploadFile_Sucesso() throws IOException {
        // Criar um arquivo temporário para o teste
        Path tempFilePath = Files.createTempFile("test-file", ".mp4");
        File tempFile = tempFilePath.toFile();
        tempFile.deleteOnExit();  // Garante que o arquivo será deletado após o teste

        // Dados de entrada para o teste
        String filePath = tempFile.getAbsolutePath();
        String fileName = "file.mp4";
        String bucketName = "test-bucket";

        // Chama o método que estamos testando
        String uploadedFilePath = s3Adapter.uploadFile(filePath, fileName, bucketName);

        // Verificação
        assertNotNull(uploadedFilePath);
        assertEquals(filePath, uploadedFilePath, "O caminho do arquivo enviado não corresponde.");

        // Verifica se o método putObject foi chamado uma vez com o PutObjectRequest correto
        verify(s3Client, times(1)).putObject(any(PutObjectRequest.class), eq(tempFilePath));
    }

    @Test
    public void testUploadFile_ArquivoNaoEncontrado() {
        // Dados de entrada para o teste
        String filePath = "path/to/nonexistent/file.mp4";
        String fileName = "file.mp4";
        String bucketName = "test-bucket";

        // Chama o método e verifica a exceção
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            s3Adapter.uploadFile(filePath, fileName, bucketName);
        });

        // Verifica a mensagem da exceção
        assertEquals("Arquivo não encontrado para upload: file.mp4", exception.getMessage());
    }
}
