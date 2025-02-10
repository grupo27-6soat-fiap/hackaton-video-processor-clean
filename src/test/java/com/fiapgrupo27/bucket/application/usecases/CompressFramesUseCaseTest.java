package com.fiapgrupo27.bucket.application.usecases;

import com.fiapgrupo27.bucket.application.gateways.ArquivoGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class CompressFramesUseCaseTest {

    @Mock
    private ArquivoGateway arquivoGateway;

    private CompressFramesUseCase compressFramesUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        compressFramesUseCase = new CompressFramesUseCase(arquivoGateway);
    }

    @Test
    void testCompactarFrames() throws IOException {
        // Dados simulados
        String outputDir = "output";
        String videoPath = "video.mp4";
        String fileNameReplaced = "video_compressed";
        String expectedPath = "output/video_compressed.zip";

        // Configuração do mock
        when(arquivoGateway.compactarFrames(anyString(), anyString(), anyString())).thenReturn(expectedPath);

        // Executa o método
        String result = compressFramesUseCase.compactarFrames(outputDir, videoPath, fileNameReplaced);

        // Verifica se o retorno é o esperado
        assertEquals(expectedPath, result);
    }
}
