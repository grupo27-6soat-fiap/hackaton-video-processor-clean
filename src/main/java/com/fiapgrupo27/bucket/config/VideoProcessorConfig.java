package com.fiapgrupo27.bucket.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiapgrupo27.bucket.application.gateways.ArquivoGateway;
import com.fiapgrupo27.bucket.application.gateways.BucketGateway;
import com.fiapgrupo27.bucket.application.gateways.SolicitacaoGateway;
import com.fiapgrupo27.bucket.application.usecases.*;
import com.fiapgrupo27.bucket.infrastructure.messaging.SQSListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.CreateQueueRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;

import java.net.URI;

@Configuration
public class VideoProcessorConfig {

    private final String queueName;
    private final String aws_region;
    private final String aws_accesskey;
    private final String aws_keyid;
    private final String aws_endpoint;

    public VideoProcessorConfig(@Value("${cloud.aws.sqs.queue}")String queueName, @Value("${cloud.aws.region}")String aws_region, @Value("${cloud.aws.accesskey}")String awsAccesskey, @Value("${cloud.aws.keyid}")String awsKeyid, @Value("${cloud.aws.endpoint}")String aws_endpoint) {
        this.queueName = queueName;
        this.aws_region = aws_region;
        this.aws_accesskey = awsAccesskey;
        this.aws_keyid = awsKeyid;
        this.aws_endpoint = aws_endpoint;
    }

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(System.getenv().getOrDefault("AWS_REGION", aws_region)))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(
                                System.getenv().getOrDefault("AWS_ACCESS_KEY_ID", aws_keyid),
                                System.getenv().getOrDefault("AWS_SECRET_ACCESS_KEY", aws_accesskey)
                        )
                ))
//                .endpointOverride(URI.create(endpoint))
                .forcePathStyle(true) // Necessário para LocalStack
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
        String endpoint = System.getenv().getOrDefault("AWS_ENDPOINT_URL", aws_endpoint);
        if (endpoint.contains("amazonaws.com")) {
            endpoint = String.format(endpoint, "sqs", aws_region);;
        }

        System.out.println("ENDPOINT SQS ======== " + endpoint);



        return SqsClient.builder()
                .region(Region.of(System.getenv().getOrDefault("AWS_REGION", aws_region)))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(
                                System.getenv().getOrDefault("AWS_ACCESS_KEY_ID", aws_keyid),
                                System.getenv().getOrDefault("AWS_SECRET_ACCESS_KEY", aws_accesskey)
                        )
                ))
//                .endpointOverride(URI.create(endpoint))
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
        String queueUrl;
        try {
            queueUrl = sqsClient.getQueueUrl(GetQueueUrlRequest.builder().queueName(queueName).build()).queueUrl();
            System.out.println("===================  QUEUE URL: " + queueUrl);
        } catch (Exception e) {

            queueUrl = sqsClient.createQueue(CreateQueueRequest.builder().queueName(queueName).build()).queueUrl();
            System.out.println("===================  QUEUE URL: " + queueUrl);
        }
        return queueUrl;
    }

}
