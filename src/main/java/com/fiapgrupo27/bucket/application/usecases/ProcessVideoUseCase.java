package com.fiapgrupo27.bucket.application.usecases;

import com.fiapgrupo27.bucket.application.dto.AtualizarStatusDTO;
import com.fiapgrupo27.bucket.domain.enums.StatusSolicitacao;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class ProcessVideoUseCase {
    private final DownloadFileUseCase downloadFileUseCase;
    private final ExtractFramesUseCase extractFramesUseCase;
    private final CompressFramesUseCase compressFramesUseCase;
    private final RemoveFramesUseCase removeFramesUseCase;
    private final AtualizarStatusUseCase atualizarStatusUseCase;
    private final UploadFileUseCase uploadFileUseCase;
    private final EnviaEmailUseCase enviaEmailUseCase;


    public ProcessVideoUseCase(DownloadFileUseCase downloadFileUseCase, ExtractFramesUseCase extractFramesUseCase, CompressFramesUseCase compressFramesUseCase, RemoveFramesUseCase removeFramesUseCase, AtualizarStatusUseCase atualizarStatusUseCase, UploadFileUseCase uploadFileUseCase, EnviaEmailUseCase enviaEmailUseCase) {

        this.downloadFileUseCase = downloadFileUseCase;
        this.extractFramesUseCase = extractFramesUseCase;
        this.compressFramesUseCase = compressFramesUseCase;
        this.removeFramesUseCase = removeFramesUseCase;
        this.atualizarStatusUseCase = atualizarStatusUseCase;
        this.uploadFileUseCase = uploadFileUseCase;
        this.enviaEmailUseCase = enviaEmailUseCase;
    }

    public void processarVideo(String idSolicitacao, String nomeArquivo,String idArquivo, String email) {

        Path arquivoBaixado = downloadFileUseCase.downloadFile(nomeArquivo);
        String videoPath = arquivoBaixado.toString(); // Caminho do vídeo no disco
        String outputDir = videoPath + "_frames"; // Diretório de saída dos frames
        String baseName = nomeArquivo.substring(0, nomeArquivo.lastIndexOf(".")); // No
        String outputDirFixed = Path.of(outputDir).toAbsolutePath().toString();
        String baseNameReplaced = baseName.replace("/", "-");
        Path outputPath = Path.of(outputDir);
        if (!Files.exists(outputPath)) {
            try {
                Files.createDirectories(outputPath); // Cria o diretório e todos os subdiretórios necessários

                extractFramesUseCase.extrairFrames(videoPath, outputDirFixed, baseNameReplaced);


                String compactarArquivoRetorno = compressFramesUseCase.compactarFrames(outputDir, videoPath, baseNameReplaced);

                // Extrair a pasta de origem do bucket (ex: "45" de "45/canyon.mp4")
                String pastaBucket = nomeArquivo.substring(0, nomeArquivo.indexOf("/"));
                String destinoS3 = pastaBucket + "/" + new File(compactarArquivoRetorno).getName();

                // Upload Arquivo Compactado
                uploadFileUseCase.uploadArquivo(compactarArquivoRetorno, destinoS3);

                // Atualizar Status para CONCLUIDO
                AtualizarStatusDTO statusDTO = new AtualizarStatusDTO(idSolicitacao, idArquivo, StatusSolicitacao.CONCLUIDO);
                atualizarStatusUseCase.atualizarStatus(statusDTO);


                // Remover Sujeira
                removeFramesUseCase.removerFrames(outputDir, videoPath, baseNameReplaced);

                enviaEmailUseCase.enviaEmailComStatus(email, idSolicitacao.toString(),"Geração realizada com sucesso! " + destinoS3);

            } catch (IOException | InterruptedException e) {
                enviaEmailUseCase.enviaEmailComStatus(email, idSolicitacao.toString(), "Ocorreu um erro na geração do arquivo: " + arquivoBaixado.toString());
                throw new RuntimeException(e);
            }
        }

    }
}
