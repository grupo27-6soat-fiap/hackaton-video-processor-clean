package com.fiapgrupo27.bucket.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiapgrupo27.bucket.application.gateways.ArquivoGateway;
import com.fiapgrupo27.bucket.application.gateways.BucketGateway;
import com.fiapgrupo27.bucket.application.gateways.SolicitacaoGateway;
import com.fiapgrupo27.bucket.application.usecases.*;
import com.fiapgrupo27.bucket.infrastructure.gateways.BucketGatewayImpl;
import com.fiapgrupo27.bucket.infrastructure.messaging.SQSListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;

import java.net.URI;

@Configuration
public class VideoProcessorConfig {

    private final String queueName;

    public VideoProcessorConfig(@Value("${cloud.aws.sqs.queue}")String queueName) {
        this.queueName = queueName;
    }

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.US_EAST_1) // Mesma região do LocalStack
                .endpointOverride(URI.create("http://localhost:4566")) // Aponta para o LocalStack
                .forcePathStyle(true) // Evita redirecionamentos desnecessários
                .build();
    }



    @Bean
    public DownloadFileUseCase downloadFileUseCase(BucketGateway bucketGateway) {
        return new DownloadFileUseCase(bucketGateway); // Passando a dependência correta
    }

    @Bean
    public ExtractFramesUseCase extractFramesUseCase(ArquivoGateway arquivoGateway) {
        return new ExtractFramesUseCase(arquivoGateway);
    }

    @Bean
    public CompressFramesUseCase compressFramesUseCase(ArquivoGateway arquivoGateway) {
        return new CompressFramesUseCase(arquivoGateway);
    }

    @Bean
    public RemoveFramesUseCase removeFramesUseCase(ArquivoGateway arquivoGateway) {
        return new RemoveFramesUseCase(arquivoGateway);
    }

    @Bean
    public AtualizarStatusUseCase atualizarStatusUseCase(SolicitacaoGateway solicitacaoGateway) {
        return new AtualizarStatusUseCase(solicitacaoGateway);
    }

    @Bean
    public UploadFileUseCase uploadFileUseCase(BucketGateway bucketGateway) {
        return new UploadFileUseCase(bucketGateway); // Supondo que precise do BucketGateway
    }

    @Bean
    public ProcessVideoUseCase processVideoUseCase(
            DownloadFileUseCase downloadFileUseCase,
            ExtractFramesUseCase extractFramesUseCase,
            CompressFramesUseCase compressFramesUseCase,
            RemoveFramesUseCase removeFramesUseCase,
            AtualizarStatusUseCase atualizarStatusUseCase,
            UploadFileUseCase uploadFileUseCase) {

        return new ProcessVideoUseCase(
                downloadFileUseCase,
                extractFramesUseCase,
                compressFramesUseCase,
                removeFramesUseCase,
                atualizarStatusUseCase,
                uploadFileUseCase
        );
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate(); // Cria um Bean para ser injetado automaticamente
    }

    @Bean
    public SqsClient sqsClient() {
        return SqsClient.builder()
                .region(Region.US_EAST_1) // Mesma região do LocalStack
                .endpointOverride(URI.create("http://localhost:4566")) // Apontando para o LocalStack
                .build();
    }

    @Bean
    public SQSListener sqsListener(ProcessVideoUseCase processVideoUseCase,
                                   @Value("${cloud.aws.sqs.queue-url}") String queueUrl,
                                   SqsClient sqsClient,
                                   ObjectMapper objectMapper) {
        return new SQSListener(processVideoUseCase, queueUrl, sqsClient, objectMapper);
    }

    @Bean
    public String createQueue(SqsClient sqsClient) {
        try {
            String queueUrl = sqsClient.getQueueUrl(GetQueueUrlRequest.builder().queueName(queueName).build()).queueUrl();
            return queueUrl;
        } catch (Exception e) {

            String queueUrl = sqsClient.createQueue(CreateQueueRequest.builder().queueName(queueName).build()).queueUrl();
            return queueUrl;
        }
    }

}
