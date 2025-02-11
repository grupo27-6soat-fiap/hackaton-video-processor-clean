package com.fiapgrupo27.bucket.application.usecases;

import com.fiapgrupo27.bucket.application.gateways.ArquivoGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import java.io.IOException;

class ExtractFramesUseCaseTest {

    @Mock
    private ArquivoGateway arquivoGateway;

    @InjectMocks
    private ExtractFramesUseCase extractFramesUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testExtrairFrames() throws IOException, InterruptedException {
        String videoPath = "caminho/do/video.mp4";
        String outputDir = "caminho/de/saida";
        String baseName = "frame";

        doNothing().when(arquivoGateway).extrairFrames(videoPath, outputDir, baseName);

        extractFramesUseCase.extrairFrames(videoPath, outputDir, baseName);

        verify(arquivoGateway, times(1)).extrairFrames(videoPath, outputDir, baseName);
    }
}
