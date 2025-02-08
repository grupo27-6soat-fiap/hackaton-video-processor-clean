package com.fiapgrupo27.bucket.infrastructure.gateways;

import com.fiapgrupo27.bucket.application.gateways.SolicitacaoGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SolicitacaoGatewayImpl implements SolicitacaoGateway {
    public final RestTemplate restTemplate;
    public final String url;

    public SolicitacaoGatewayImpl(RestTemplate restTemplate, @Value("${aws.solicitacao.service.url}")String url) {
        this.restTemplate = restTemplate;
        this.url = url;

    }

    @Override
    public void atualizarStatus(Long idSolicitacao, Long idArquivo, String status) {
        try {
            String urlFinal = String.format(url,idSolicitacao, idArquivo, status);
            ResponseEntity<String> response = restTemplate.exchange(
                    urlFinal,
                    HttpMethod.PUT,
                    HttpEntity.EMPTY,
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Status atualizado com sucesso!");
            } else {
                System.err.println("Falha ao atualizar status: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.err.println("Erro ao chamar o serviço de atualização de status: " + e.getMessage());
        }

    }
}
