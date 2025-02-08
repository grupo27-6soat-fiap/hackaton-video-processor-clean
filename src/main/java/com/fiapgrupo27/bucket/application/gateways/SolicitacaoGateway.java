package com.fiapgrupo27.bucket.application.gateways;

public interface SolicitacaoGateway {
    void atualizarStatus(Long idSolicitacao, Long idArquivo, String status);
}
