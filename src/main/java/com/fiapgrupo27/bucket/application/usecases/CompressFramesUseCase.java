package com.fiapgrupo27.bucket.application.usecases;

import com.fiapgrupo27.bucket.application.gateways.ArquivoGateway;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class CompressFramesUseCase {

    private final ArquivoGateway arquivoGateway;

    public CompressFramesUseCase(ArquivoGateway arquivoGateway) {
        this.arquivoGateway = arquivoGateway;
    }
    public String compactarFrames(String outputDir, String videoPath, String fileNameReplaced) throws IOException {
        return arquivoGateway.compactarFrames(outputDir, videoPath, fileNameReplaced);
    }


}
