package br.ufsm.csi.pilacoin.service;

import br.ufsm.csi.pilacoin.model.PilaCoin;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class PilaCoinService extends SwaggerService {

    private final String url = "http://" + serverAddress + "/pilacoin/";

    public void enviar(PilaCoin pilaCoin) {

        HttpEntity<PilaCoin> entidade = new HttpEntity<>(pilaCoin, headers);

        try {
            ResponseEntity<PilaCoin> response = template.postForEntity(url, entidade, PilaCoin.class);
            System.out.println(response.getBody());
        } catch (HttpClientErrorException e) {
            throw new RuntimeException(e);
        }
    }

}
