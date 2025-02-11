package com.fiapgrupo27.bucket.application.usecases;

import com.fiapgrupo27.bucket.application.gateways.EmailGateway;

public class EnviaEmailUseCase {
    private final EmailGateway emailGateway;

    public EnviaEmailUseCase(EmailGateway emailGateway) {
        this.emailGateway = emailGateway;
    }

    public void enviaEmailComStatus(String destinatario, String assunto, String mensagem){
        emailGateway.enviarEmail(destinatario, assunto, mensagem);
    }
}
