package com.fiapgrupo27.bucket.application.gateways;

public interface EmailGateway {
    void enviarEmail(String destinatario, String assunto, String mensagem);
}
