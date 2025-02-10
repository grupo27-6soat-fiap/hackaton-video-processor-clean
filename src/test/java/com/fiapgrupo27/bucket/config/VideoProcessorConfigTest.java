package com.fiapgrupo27.bucket.config;

import com.fiapgrupo27.bucket.application.gateways.ArquivoGateway;
import com.fiapgrupo27.bucket.application.gateways.BucketGateway;
import com.fiapgrupo27.bucket.application.gateways.SolicitacaoGateway;
import com.fiapgrupo27.bucket.application.usecases.*;
import com.fiapgrupo27.bucket.infrastructure.messaging.SQSListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VideoProcessorConfigTest {

    @Mock
    private BucketGateway bucketGateway;
    
    @Mock
    private ArquivoGateway arquivoGateway;

    @Mock
    private SolicitacaoGateway solicitacaoGateway;
    
    @Mock
    private S3Client s3Client;

    @Mock
    private SqsClient sqsClient;
    
    private VideoProcessorConfig videoProcessorConfig;
    
    @BeforeEach
    void setUp() {
        // Criando um objeto de configuração com valores fictícios para os parâmetros
        videoProcessorConfig = new VideoProcessorConfig(
            "testQueueName", "us-east-1", "testAccessKey", "testSecretKey", "http://localhost"
        );
    }

    @Test
    void testS3ClientBeanCreation() {
        // Testando se o S3Client é criado corretamente
        S3Client createdS3Client = videoProcessorConfig.s3Client();
        assertNotNull(createdS3Client);
    }

    @Test
    void testSqsClientBeanCreation() {
        // Testando se o SQS Client é criado corretamente
        SqsClient createdSqsClient = videoProcessorConfig.sqsClient();
        assertNotNull(createdSqsClient);
    }

    @Test
    void testDownloadFileUseCaseBeanCreation() {
        // Testando se o DownloadFileUseCase é criado corretamente
        DownloadFileUseCase downloadFileUseCase = videoProcessorConfig.downloadFileUseCase(bucketGateway);
        assertNotNull(downloadFileUseCase);
    }

    @Test
    void testProcessVideoUseCaseBeanCreation() {
        // Testando se o ProcessVideoUseCase é criado corretamente
        ProcessVideoUseCase processVideoUseCase = videoProcessorConfig.processVideoUseCase(
            videoProcessorConfig.downloadFileUseCase(bucketGateway),
            videoProcessorConfig.extractFramesUseCase(arquivoGateway),
            videoProcessorConfig.compressFramesUseCase(arquivoGateway),
            videoProcessorConfig.removeFramesUseCase(arquivoGateway),
            videoProcessorConfig.atualizarStatusUseCase(solicitacaoGateway),
            videoProcessorConfig.uploadFileUseCase(bucketGateway)
        );
        assertNotNull(processVideoUseCase);
    }

    @Test
    void testCreateQueue() {
        // Uso de lenient() para evitar UnnecessaryStubbingException
        lenient().when(sqsClient.getQueueUrl(any(GetQueueUrlRequest.class)))
                .thenReturn(GetQueueUrlResponse.builder().queueUrl("test-queue-url").build());

        lenient().when(sqsClient.createQueue(any(CreateQueueRequest.class)))
                .thenReturn(CreateQueueResponse.builder().queueUrl("test-queue-url").build());

        // Testando a criação de fila
        String queueUrl = videoProcessorConfig.createQueue(sqsClient);
        assertNotNull(queueUrl);
        assertEquals("test-queue-url", queueUrl);
    }
}
