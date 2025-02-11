package com.fiapgrupo27.bucket.application.usecases;

import com.fiapgrupo27.bucket.application.gateways.ArquivoGateway;
import com.fiapgrupo27.bucket.application.usecases.RemoveFramesUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

class RemoveFramesUseCaseTest {

    @Mock
    private ArquivoGateway arquivoGateway;

    @InjectMocks
    private RemoveFramesUseCase removeFramesUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRemoverFrames() throws IOException {
        String outputDir = "test/output";
        String videoPath = "test/video.mp4";
        String nameFileReplaced = "video_processed";

        doNothing().when(arquivoGateway).removerFrames(outputDir, videoPath, nameFileReplaced);

        removeFramesUseCase.removerFrames(outputDir, videoPath, nameFileReplaced);

        verify(arquivoGateway).removerFrames(outputDir, videoPath, nameFileReplaced);
    }
}
