package com.fiapgrupo27.bucket.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BucketFileTest {

    @Test
    public void testGetFileName() {
        BucketFile bucketFile = new BucketFile("video.mp4", "http://example.com/video.mp4");
        assertEquals("video.mp4", bucketFile.getFileName());
    }

    @Test
    public void testGetFileUrl() {
        BucketFile bucketFile = new BucketFile("video.mp4", "http://example.com/video.mp4");
        assertEquals("http://example.com/video.mp4", bucketFile.getFileUrl());
    }

    @Test
    public void testConstructor() {
        String fileName = "image.jpg";
        String fileUrl = "http://example.com/image.jpg";
        
        BucketFile bucketFile = new BucketFile(fileName, fileUrl);
        
        assertNotNull(bucketFile);
        assertEquals(fileName, bucketFile.getFileName());
        assertEquals(fileUrl, bucketFile.getFileUrl());
    }
}
