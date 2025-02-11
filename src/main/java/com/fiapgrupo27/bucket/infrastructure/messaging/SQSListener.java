package com.fiapgrupo27.bucket.infrastructure.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiapgrupo27.bucket.application.usecases.ProcessVideoUseCase;
import com.fiapgrupo27.bucket.domain.BucketFile;
import org.springframework.beans.factory.annotation.Value;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class SQSListener {
    private final ProcessVideoUseCase processVideoUseCase;
    private final String queueUrl;
    private final SqsClient sqsClient;
    private final ObjectMapper objectMapper;

    public SQSListener(ProcessVideoUseCase processVideoUseCase, @Value("${cloud.aws.sqs.queue-url}") String queueUrl, SqsClient sqsClient, ObjectMapper objectMapper) {
        this.processVideoUseCase = processVideoUseCase;
        this.queueUrl = System.getenv().getOrDefault("AWS_SQS_QUEUE_URL", queueUrl);;
        this.sqsClient = sqsClient;
        this.objectMapper = objectMapper;
    }



    @Scheduled(fixedDelay = 1000) // Checa a fila a cada 5 segundos
    public void listenToSqs() {
        ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                .queueUrl(queueUrl)
                .maxNumberOfMessages(5)
                .waitTimeSeconds(5)
                .build();

        List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).messages();

        for (Message message : messages) {

            processMessage(message.body());

            // Deletar a mensagem após o processamento
            DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .receiptHandle(message.receiptHandle())
                    .build();
            sqsClient.deleteMessage(deleteMessageRequest);
        }
    }

    private void processMessage(String messageBody) {
        System.out.println("Received message: " + messageBody);

        try {
            Map<String, Object> mensagem = objectMapper.readValue(messageBody, new TypeReference<Map<String, Object>>() {});
            // Recuperar os valores do JSON
            Integer idSolicitacao = (Integer) mensagem.get("idSolicitacao");
            String nomeArquivo = (String) mensagem.get("nomeArquivo");
            String idArquivo = String.valueOf(mensagem.get("idArquivo"));
            String email = String.valueOf(mensagem.get("email"));
            processVideoUseCase.processarVideo(idSolicitacao.toString(), nomeArquivo, idArquivo, email);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }
}
