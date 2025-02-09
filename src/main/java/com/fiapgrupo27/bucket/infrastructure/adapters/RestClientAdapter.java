package com.fiapgrupo27.bucket.infrastructure.adapters;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestClientAdapter {
    private final RestTemplate restTemplate;

    public RestClientAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void makeRequest(Long idSolicitacao, Long idArquivo, String status, String url) {
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
