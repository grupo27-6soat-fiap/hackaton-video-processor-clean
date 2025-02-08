package com.fiapgrupo27.bucket.application.usecases;

import com.fiapgrupo27.bucket.application.gateways.ArquivoGateway;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class RemoveFramesUseCase {
    private final ArquivoGateway arquivoGateway;

    public RemoveFramesUseCase(ArquivoGateway arquivoGateway) {
        this.arquivoGateway = arquivoGateway;
    }

    public void removerFrames(String outputDir, String videoPath, String nameFileReplaced) throws IOException {
        arquivoGateway.removerFrames(outputDir, videoPath, nameFileReplaced);

    }
}
