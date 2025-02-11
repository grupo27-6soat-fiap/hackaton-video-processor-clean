package com.fiapgrupo27.bucket.infrastructure.adapters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

@ExtendWith(MockitoExtension.class)
public class RestClientAdapterTest {

    @Mock
    private RestTemplate restTemplate;

    private RestClientAdapter restClientAdapter;

    @BeforeEach
    public void setUp() {
        restClientAdapter = new RestClientAdapter(restTemplate);
    }

    @Test
    public void testMakeRequest_Success() {
        // Simula resposta de sucesso
        ResponseEntity<String> responseEntity = ResponseEntity.ok("Sucesso");
        when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT), eq(HttpEntity.EMPTY), eq(String.class)))
                .thenReturn(responseEntity);

        // Chama o método
        restClientAdapter.makeRequest(1L, 2L, "status", "http://exemplo.com/{0}/{1}/{2}");

        // Verifica se o método foi chamado
        verify(restTemplate).exchange(anyString(), eq(HttpMethod.PUT), eq(HttpEntity.EMPTY), eq(String.class));
    }

    @Test
    public void testMakeRequest_FalhaAoAtualizarStatus() {
        Long idSolicitacao = 1L;
        Long idArquivo = 1L;
        String status = "FAILED";
        String url = "http://example.com/update/%d/%d/%s";

        // Simulando uma resposta de erro HTTP 500 (erro interno do servidor)
        ResponseEntity<String> response = new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT), eq(HttpEntity.EMPTY), eq(String.class)))
                .thenReturn(response);

        // Captura da saída do erro para verificar se foi impresso corretamente no console
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setErr(new PrintStream(baos));

        // Executando o método
        restClientAdapter.makeRequest(idSolicitacao, idArquivo, status, url);

        // Verificando a saída de erro no console
        String expectedMessage = "Falha ao atualizar status: 500 INTERNAL_SERVER_ERROR";
        assertTrue(baos.toString().contains(expectedMessage));

        // Verificar se o RestTemplate foi chamado corretamente
        verify(restTemplate).exchange(eq("http://example.com/update/1/1/FAILED"), eq(HttpMethod.PUT), eq(HttpEntity.EMPTY), eq(String.class));
    }

    @Test
    public void testMakeRequest_ExceptionDuranteRequisicao() {
        Long idSolicitacao = 1L;
        Long idArquivo = 1L;
        String status = "FAILED";
        String url = "http://example.com/update/%d/%d/%s";

        // Simulando uma exceção durante a execução da requisição
        when(restTemplate.exchange(anyString(), eq(HttpMethod.PUT), eq(HttpEntity.EMPTY), eq(String.class)))
                .thenThrow(new RuntimeException("Erro ao chamar o serviço"));

        // Captura da saída do erro para verificar se foi impresso corretamente no console
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setErr(new PrintStream(baos));

        // Executando o método
        restClientAdapter.makeRequest(idSolicitacao, idArquivo, status, url);

        // Verificando a saída de erro no console
        String expectedMessage = "Erro ao chamar o serviço de atualização de status: Erro ao chamar o serviço";
        assertTrue(baos.toString().contains(expectedMessage));

        // Verificar que o RestTemplate foi chamado corretamente
        verify(restTemplate).exchange(eq("http://example.com/update/1/1/FAILED"), eq(HttpMethod.PUT), eq(HttpEntity.EMPTY), eq(String.class));
    }
}
