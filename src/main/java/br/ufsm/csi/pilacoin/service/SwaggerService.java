package br.ufsm.csi.pilacoin.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SwaggerService {


    // @Value("${servidor.url}")
    protected String serverAddress = "srv-ceesp.proj.ufsm.br:8097";

    protected final HttpHeaders headers = new HttpHeaders();
    protected final RestTemplate template = new RestTemplate();

    protected SwaggerService() {
        headers.setContentType(MediaType.APPLICATION_JSON);
    }
}
