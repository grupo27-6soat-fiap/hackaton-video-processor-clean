package com.fiapgrupo27.bucket.infrastructure.gateways;

import com.fiapgrupo27.bucket.application.gateways.EmailGateway;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.*;

@Component
public class EmailGatewaySESImpl implements EmailGateway {
    private final SesClient sesClient;
    private final String aws_endpoint;

    public EmailGatewaySESImpl(SesClient sesClient, String awsEndpoint) {
        this.sesClient = sesClient;
        aws_endpoint = awsEndpoint;
    }

    @Override
    public void enviarEmail(String destinatario, String assunto, String mensagem) {
//        String newAwsEndpoint = System.getenv().getOrDefault("AWS_ENDPOINT_URL", aws_endpoint);
        SendEmailRequest request = SendEmailRequest.builder()
                .destination(Destination.builder().toAddresses(destinatario).build())
                .message(Message.builder()
                        .subject(Content.builder().data(assunto).charset("UTF-8").build())
                        .body(Body.builder()
                                .text(Content.builder().data(mensagem).charset("UTF-8").build())
                                .build())
                        .build())
                .source("felipereis1992@gmail.com")
                .build();

        sesClient.sendEmail(request);
    }
}
