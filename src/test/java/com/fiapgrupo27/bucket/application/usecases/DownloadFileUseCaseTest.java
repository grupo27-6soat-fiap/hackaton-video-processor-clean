package com.fiapgrupo27.bucket.application.usecases;

import com.fiapgrupo27.bucket.application.gateways.BucketGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class DownloadFileUseCaseTest {

    @Mock
    private BucketGateway bucketGateway;

    @InjectMocks
    private DownloadFileUseCase downloadFileUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDownloadFile() {
        String nomeArquivo = "arquivo.txt";
        Path caminhoEsperado = Path.of("/caminho/do/arquivo.txt");

        when(bucketGateway.downloadFile(nomeArquivo)).thenReturn(caminhoEsperado);

        Path resultado = downloadFileUseCase.downloadFile(nomeArquivo);

        assertEquals(caminhoEsperado, resultado);
        verify(bucketGateway, times(1)).downloadFile(nomeArquivo);
    }
}