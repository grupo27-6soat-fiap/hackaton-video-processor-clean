package com.fiapgrupo27.bucket.application.usecases;

import com.fiapgrupo27.bucket.application.dto.AtualizarStatusDTO;
import com.fiapgrupo27.bucket.application.gateways.SolicitacaoGateway;
import com.fiapgrupo27.bucket.application.usecases.AtualizarStatusUseCase;
import com.fiapgrupo27.bucket.domain.enums.StatusSolicitacao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AtualizarStatusUseCaseTest {

    @Mock
    private SolicitacaoGateway solicitacaoGateway;

    private AtualizarStatusUseCase atualizarStatusUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        atualizarStatusUseCase = new AtualizarStatusUseCase(solicitacaoGateway);
    }

    @Test
    void testAtualizarStatus() {
        // Criar um objeto DTO para simular a atualização de status
        AtualizarStatusDTO dto = new AtualizarStatusDTO("1", "100", StatusSolicitacao.CONCLUIDO);

        // Chamar o método a ser testado
        atualizarStatusUseCase.atualizarStatus(dto);

        // Verificar se o método foi chamado corretamente com os valores esperados
        verify(solicitacaoGateway).atualizarStatus(dto.getIdSolicitacaoLong(), dto.getIdArquivoLong(), dto.getStatusString());
    }
}
