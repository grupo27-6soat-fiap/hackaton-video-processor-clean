package com.fiapgrupo27.bucket.application.usecases;

import com.fiapgrupo27.bucket.application.gateways.ArquivoGateway;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ExtractFramesUseCase {
    private final ArquivoGateway arquivoGateway;

    public ExtractFramesUseCase(ArquivoGateway arquivoGateway) {
        this.arquivoGateway = arquivoGateway;
    }

    public void extrairFrames(String videoPath, String outputDir, String baseName) throws IOException, InterruptedException {
        arquivoGateway.extrairFrames(videoPath, outputDir, baseName);


    }
}