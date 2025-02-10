package com.fiapgrupo27.bucket.application.usecases;

import com.fiapgrupo27.bucket.application.dto.AtualizarStatusDTO;
import com.fiapgrupo27.bucket.application.gateways.ArquivoGateway;
import com.fiapgrupo27.bucket.domain.enums.StatusSolicitacao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProcessVideoUseCaseTest {

    @Mock
    private DownloadFileUseCase downloadFileUseCase;
    
    @Mock
    private ExtractFramesUseCase extractFramesUseCase;
    
    @Mock
    private CompressFramesUseCase compressFramesUseCase;
    
    @Mock
    private RemoveFramesUseCase removeFramesUseCase;
    
    @Mock
    private AtualizarStatusUseCase atualizarStatusUseCase;
    
    @Mock
    private UploadFileUseCase uploadFileUseCase;

    private ProcessVideoUseCase processVideoUseCase;

    @BeforeEach
    void setUp() {
        // Inicia o mock e a classe a ser testada
        MockitoAnnotations.openMocks(this);
        processVideoUseCase = new ProcessVideoUseCase(
                downloadFileUseCase, 
                extractFramesUseCase, 
                compressFramesUseCase, 
                removeFramesUseCase, 
                atualizarStatusUseCase, 
                uploadFileUseCase
        );
    }

    @Test
    void testProcessarVideo() throws Exception {
        // Dado que o arquivo é baixado corretamente
        Path filePath = Files.createTempFile("test-video", ".mp4");
        when(downloadFileUseCase.downloadFile(any())).thenReturn(filePath);

        // Simular sucesso na extração de frames
        doNothing().when(extractFramesUseCase).extrairFrames(any(), any(), any());

        // Simular sucesso na compactação do arquivo
        when(compressFramesUseCase.compactarFrames(any(), any(), any())).thenReturn("compressed-file-path");

        // Simular sucesso no upload do arquivo
        doNothing().when(uploadFileUseCase).uploadArquivo(any(), any());

        // Simular sucesso na atualização de status
        doNothing().when(atualizarStatusUseCase).atualizarStatus(any(AtualizarStatusDTO.class));

        // Simular sucesso na remoção dos frames
        doNothing().when(removeFramesUseCase).removerFrames(any(), any(), any());

        // Testar o método processarVideo
        processVideoUseCase.processarVideo("1", "video/filename.mp4", "123");

        // Verificar interações com os mocks
        verify(downloadFileUseCase).downloadFile("video/filename.mp4");
        verify(extractFramesUseCase).extrairFrames(any(), any(), any());
        verify(compressFramesUseCase).compactarFrames(any(), any(), any());
        verify(uploadFileUseCase).uploadArquivo(any(), any());
        verify(atualizarStatusUseCase).atualizarStatus(any(AtualizarStatusDTO.class));
        verify(removeFramesUseCase).removerFrames(any(), any(), any());
    }

    @Test
    void testProcessarVideoThrowsException() throws Exception {
        // Dado que o download do arquivo falha
        when(downloadFileUseCase.downloadFile(any())).thenThrow(new RuntimeException("Download failed"));

        // Testar o método processarVideo e garantir que ele lança uma exceção
        try {
            processVideoUseCase.processarVideo("1", "video/filename.mp4", "123");
        } catch (RuntimeException e) {
            // Verificar se a exceção foi lançada corretamente
            assert e.getMessage().equals("Download failed");
        }

        // Verificar que os outros métodos não foram chamados
        verify(extractFramesUseCase, never()).extrairFrames(any(), any(), any());
        verify(compressFramesUseCase, never()).compactarFrames(any(), any(), any());
        verify(uploadFileUseCase, never()).uploadArquivo(any(), any());
        verify(atualizarStatusUseCase, never()).atualizarStatus(any(AtualizarStatusDTO.class));
        verify(removeFramesUseCase, never()).removerFrames(any(), any(), any());
    }

}
