package com.fiapgrupo27.bucket.infrastructure.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiapgrupo27.bucket.application.usecases.ProcessVideoUseCase;

import com.fasterxml.jackson.core.type.TypeReference;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SQSListenerTest {

    @Mock
    private ProcessVideoUseCase processVideoUseCase;

    @Mock
    private SqsClient sqsClient;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private SQSListener sqsListener;

    private final String queueUrl = "https://sqs.us-east-1.amazonaws.com/123456789/test-queue";

    @BeforeEach
    void setUp() {
        sqsListener = new SQSListener(processVideoUseCase, queueUrl, sqsClient, objectMapper);
    }

    @Test
    void testListenToSqs_ComMensagemValida() throws JsonProcessingException {
        // Simulando uma mensagem válida na fila SQS
        String mensagemJson = "{\"idSolicitacao\": 1, \"nomeArquivo\": \"video.mp4\", \"idArquivo\": \"abc123\", \"email\": \"email\"}";

        Message mockMessage = Message.builder()
                .body(mensagemJson)
                .receiptHandle("mock-receipt-handle")
                .build();

        ReceiveMessageResponse mockResponse = ReceiveMessageResponse.builder()
                .messages(List.of(mockMessage))
                .build();

        // Simular retorno do SQS
        when(sqsClient.receiveMessage(any(ReceiveMessageRequest.class))).thenReturn(mockResponse);

        // Mock do ObjectMapper para garantir que retorna um Map válido
        Map<String, Object> mensagemMap = new HashMap<>();
        mensagemMap.put("idSolicitacao", 1);
        mensagemMap.put("nomeArquivo", "video.mp4");
        mensagemMap.put("idArquivo", "abc123");

        when(objectMapper.readValue(eq(mensagemJson), any(TypeReference.class))).thenReturn(mensagemMap);

        // Executa o método a ser testado
        sqsListener.listenToSqs();

        // Verifica se o método processarVideo foi chamado corretamente
//        verify(processVideoUseCase).processarVideo("1", "video.mp4", "abc123", "email");

        // Verifica se o método de deletar mensagem foi chamado corretamente
        verify(sqsClient).deleteMessage(any(DeleteMessageRequest.class));
    }
}
