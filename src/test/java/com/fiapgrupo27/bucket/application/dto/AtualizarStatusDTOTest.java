package com.fiapgrupo27.bucket.application.dto;

import com.fiapgrupo27.bucket.domain.enums.StatusSolicitacao;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AtualizarStatusDTOTest {

    @Test
    public void testGetIdSolicitacao() {
        AtualizarStatusDTO dto = new AtualizarStatusDTO("1", "123", StatusSolicitacao.CONCLUIDO);
        assertEquals("1", dto.getIdSolicitacao());
    }

    @Test
    public void testGetIdArquivo() {
        AtualizarStatusDTO dto = new AtualizarStatusDTO("1", "123", StatusSolicitacao.CONCLUIDO);
        assertEquals("123", dto.getIdArquivo());
    }

    @Test
    public void testGetStatus() {
        AtualizarStatusDTO dto = new AtualizarStatusDTO("1", "123", StatusSolicitacao.CONCLUIDO);
        assertEquals(StatusSolicitacao.CONCLUIDO, dto.getStatus());
    }

    @Test
    public void testGetIdSolicitacaoLong() {
        AtualizarStatusDTO dto = new AtualizarStatusDTO("1", "123", StatusSolicitacao.CONCLUIDO);
        assertEquals(1L, dto.getIdSolicitacaoLong());
    }

    @Test
    public void testGetIdArquivoLong() {
        AtualizarStatusDTO dto = new AtualizarStatusDTO("123", "456", StatusSolicitacao.CONCLUIDO);
        assertEquals(456L, dto.getIdArquivoLong());
    }

    @Test
    public void testGetStatusString() {
        AtualizarStatusDTO dto = new AtualizarStatusDTO("1", "123", StatusSolicitacao.CONCLUIDO);
        assertEquals("CONCLUIDO", dto.getStatusString());
    }
}
