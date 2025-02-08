package com.fiapgrupo27.bucket.application.gateways;

import java.io.IOException;

public interface ArquivoGateway {
    public void extrairFrames(String videoPath, String outputDir, String baseName) throws IOException, InterruptedException ;
    public String compactarFrames(String outputDir, String videoPath, String fileNameReplaced) throws IOException;
    public void removerFrames(String outputDir, String videoPath, String nameFileReplaced) throws IOException;

}
