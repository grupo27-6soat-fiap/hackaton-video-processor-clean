package com.fiapgrupo27.bucket.infrastructure.gateways;

import com.fiapgrupo27.bucket.application.gateways.SolicitacaoGateway;
import com.fiapgrupo27.bucket.infrastructure.adapters.RestClientAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SolicitacaoGatewayImpl implements SolicitacaoGateway {
    public final String url;
    private final RestClientAdapter restClientAdapter;

    public SolicitacaoGatewayImpl(@Value("${aws.solicitacao.service.url}")String url, RestClientAdapter restClientAdapter) {
        this.url = url;
        this.restClientAdapter = restClientAdapter;
    }

    @Override
    public void atualizarStatus(Long idSolicitacao, Long idArquivo, String status) {
        restClientAdapter.makeRequest(idSolicitacao, idArquivo, status, System.getenv().getOrDefault("AWS_ATUALIZARSTATUS_SERVICE", url));

    }
}
