package com.fiapgrupo27.bucket.application.dto;

import com.fiapgrupo27.bucket.domain.enums.StatusSolicitacao;

public class AtualizarStatusDTO {
    private final String idSolicitacao;
    private final String idArquivo;
    private final StatusSolicitacao status;

    public AtualizarStatusDTO(String idSolicitacao, String idArquivo, StatusSolicitacao status) {
        this.idSolicitacao = idSolicitacao;
        this.idArquivo = idArquivo;
        this.status = status;
    }

    public String getIdSolicitacao() {
        return idSolicitacao;
    }

    public String getIdArquivo() {
        return idArquivo;
    }

    public StatusSolicitacao getStatus() {
        return status;
    }

    public Long getIdSolicitacaoLong(){
        return Long.parseLong(idSolicitacao);
    }

    public Long getIdArquivoLong() {
        return Long.parseLong(idArquivo);
    }

    public String getStatusString() {
        return status.toString();
    }
}
