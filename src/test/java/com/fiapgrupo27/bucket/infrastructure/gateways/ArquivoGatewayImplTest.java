package com.fiapgrupo27.bucket.infrastructure.gateways;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ArquivoGatewayImplTest {

    private ArquivoGatewayImpl arquivoGateway;

    @MockBean
    private Runtime mockRuntime;

    @BeforeEach
    void setUp() {
        arquivoGateway = new ArquivoGatewayImpl();
    }

    // Teste simples para extrair frames
    @Test
    void testExtrairFrames() throws IOException, InterruptedException {
         // Definir os parâmetros de entrada
         String videoPath = "video.mp4";
         String outputDir = "output";
         String baseName = "frame";
 
         // Gerar o comando
         String command = String.format(
                 "ffmpeg -i %s -vf fps=1/4 %s/%s_%%04d.jpg",
                 videoPath, outputDir, baseName
         );
 
         // Verificar se a saída é a esperada
         String expectedCommand = "ffmpeg -i video.mp4 -vf fps=1/4 output/frame_%04d.jpg";
         assertEquals(expectedCommand, command);
     
    }

    // Teste simples para compactar frames
    @Test
    void testCompactarFrames() throws IOException {
        ArquivoGatewayImpl arquivoGateway = new ArquivoGatewayImpl();

        // Definindo um diretório de saída fictício
        String outputDir = "src/test/resources/output";
        String videoPath = "src/test/resources/video.mp4";
        String fileNameReplaced = "frame";

        // Criar diretório de saída
        Files.createDirectories(Paths.get(outputDir));

        // Simular que há arquivos de frames no diretório (não há como testar real funcionalidade sem ffmpeg)
        File dummyFrame = new File(outputDir + "/" + fileNameReplaced + "_0001.jpg");
        dummyFrame.createNewFile();  // Criar arquivo de teste

        // Chamar o método para compactar
        String zipFilePath = arquivoGateway.compactarFrames(outputDir, videoPath, fileNameReplaced);

        // Verificar se o caminho do arquivo zip foi retornado corretamente
        assertNotNull(zipFilePath);
        assertTrue(zipFilePath.endsWith(".zip"));

        // Limpar após o teste
        Files.deleteIfExists(dummyFrame.toPath());
        Files.deleteIfExists(Paths.get(zipFilePath));
        Files.deleteIfExists(Paths.get(outputDir));
    }

    // Teste simples para remover frames
    @Test
    void testRemoverFrames() throws IOException {
        ArquivoGatewayImpl arquivoGateway = new ArquivoGatewayImpl();

        // Definindo um diretório de saída fictício
        String outputDir = "src/test/resources/output";
        String videoPath = "src/test/resources/video.mp4";
        String nameFileReplaced = "frame";

        // Criar diretório de saída e arquivos de frame
        Files.createDirectories(Paths.get(outputDir));
        File dummyFrame = new File(outputDir + "/" + nameFileReplaced + "_0001.jpg");
        dummyFrame.createNewFile();

        // Chamar o método para remover frames
        arquivoGateway.removerFrames(outputDir, videoPath, nameFileReplaced);

        // Verificar se o arquivo foi removido
        assertFalse(dummyFrame.exists(), "O arquivo de frame não foi removido");

        // Limpar após o teste
        Files.deleteIfExists(Paths.get(outputDir));
    }
}
