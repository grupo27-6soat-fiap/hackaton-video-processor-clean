package com.fiapgrupo27.bucket.infrastructure.gateways;

import com.fiapgrupo27.bucket.application.gateways.ArquivoGateway;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class ArquivoGatewayImpl implements ArquivoGateway {
    @Override
    public void extrairFrames(String videoPath, String outputDir, String baseName) throws IOException, InterruptedException  {

        String command = String.format(
                // "ffmpeg -i %s -vf fps=1/1 %s/frame_%%04d.jpg",
                "ffmpeg -i %s -vf fps=1/4 %s/%s_%%04d.jpg",
                videoPath, outputDir, baseName
        );

        Process process = Runtime.getRuntime().exec(command);

        // Consome a saída padrão em uma thread separada
        Thread stdOutThread = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("STDOUT: " + line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Consome a saída de erro em uma thread separada
        Thread stdErrThread = new Thread(() -> {
            try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = errorReader.readLine()) != null) {
                    System.err.println("STDERR: " + line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        stdOutThread.start();
        stdErrThread.start();

        // Aguarda o término do processo
        int exitCode = process.waitFor();
        stdOutThread.join();
        stdErrThread.join();

        if (exitCode != 0) {
            throw new RuntimeException("Erro ao extrair quadros. Verifique o log para mais detalhes.");
        }

    }

    @Override
    public String compactarFrames(String outputDir, String videoPath, String fileNameReplaced) throws IOException {
        String baseName = new File(videoPath).getName().replace(".mp4", "");
        String zipFilePath = outputDir + File.separator + fileNameReplaced + ".zip";

        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFilePath))) {
            Files.list(Paths.get(outputDir))
                    .filter(path -> path.toString().endsWith(".jpg") && path.getFileName().toString().contains(fileNameReplaced))
                    .forEach(path -> {
                        try (FileInputStream fis = new FileInputStream(path.toFile())) {
                            ZipEntry zipEntry = new ZipEntry(path.getFileName().toString());
                            zipOut.putNextEntry(zipEntry);
                            byte[] buffer = new byte[1024];
                            int length;
                            while ((length = fis.read(buffer)) >= 0) {
                                zipOut.write(buffer, 0, length);
                            }
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    });
        }
        return zipFilePath;
    }

    @Override
    public void removerFrames(String outputDir, String videoPath, String nameFileReplaced) throws IOException {
        String baseName = new File(videoPath).getName().replace(".mp4", "");
        Files.list(Paths.get(outputDir))
                .filter(path -> path.toString().endsWith(".jpg") && path.getFileName().toString().contains(nameFileReplaced))
                .forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                });
    }
}
