package com.fiapgrupo27.bucket.infrastructure.gateways;

import com.fiapgrupo27.bucket.infrastructure.gateways.SolicitacaoGatewayImpl;
import com.fiapgrupo27.bucket.infrastructure.adapters.RestClientAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.mockito.Mockito.*;

class SolicitacaoGatewayImplTest {

    @Mock
    private RestClientAdapter restClientAdapter;  // Mock do RestClientAdapter

    private SolicitacaoGatewayImpl solicitacaoGateway;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Inicializa os mocks
        solicitacaoGateway = new SolicitacaoGatewayImpl("http://example.com", restClientAdapter);  // Cria a instância da classe com o mock
    }

    @Test
    void testAtualizarStatus() {
        // Dados do teste
        Long idSolicitacao = 123L;
        Long idArquivo = 456L;
        String status = "COMPLETO";

        // Chama o método a ser testado
        solicitacaoGateway.atualizarStatus(idSolicitacao, idArquivo, status);

        // Verifica se o método makeRequest foi chamado uma vez com os parâmetros corretos
        verify(restClientAdapter, times(1)).makeRequest(idSolicitacao, idArquivo, status, "http://example.com");
    }
}
