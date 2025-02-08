package com.fiapgrupo27.bucket.domain;

public class BucketFile {
    private String fileName;
    private String fileUrl;

    public BucketFile(String fileName, String fileUrl) {
        this.fileName = fileName;
        this.fileUrl = fileUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }
}