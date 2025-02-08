package com.fiapgrupo27.bucket.application.usecases;

import com.fiapgrupo27.bucket.application.dto.AtualizarStatusDTO;
import com.fiapgrupo27.bucket.application.gateways.SolicitacaoGateway;
import org.springframework.beans.factory.annotation.Value;

public class AtualizarStatusUseCase {
    private final SolicitacaoGateway solicitacaoGateway;

    public AtualizarStatusUseCase(SolicitacaoGateway solicitacaoGateway) {
        this.solicitacaoGateway = solicitacaoGateway;
    }

    public void atualizarStatus(AtualizarStatusDTO atualizarStatusDTO) {
        solicitacaoGateway.atualizarStatus(atualizarStatusDTO.getIdSolicitacaoLong(), atualizarStatusDTO.getIdArquivoLong(), atualizarStatusDTO.getStatusString());

    }
}
