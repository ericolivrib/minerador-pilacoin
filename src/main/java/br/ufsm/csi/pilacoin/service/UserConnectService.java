package br.ufsm.csi.pilacoin.service;

import br.ufsm.csi.pilacoin.util.KeysUtil;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.security.KeyPair;
import java.util.Base64;

@Service
public class UserConnectService {

    @Value("${servidor.url}") private String serverAddress;

    @PostConstruct
    public void init() {
        // System.out.println("Registrando o usuário " + registrar().nome + " na API...");
    }

    @SneakyThrows
    private RestUser registrar() {
        String url = "http://" + serverAddress + "/usuario/";

        KeyPair kp = KeysUtil.getKeyPair();
        RestUser user = RestUser.builder().nome("Érico").chavePublica(kp.getPublic().getEncoded()).build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<RestUser> entidade = new HttpEntity<>(user, headers);
        RestTemplate template = new RestTemplate();

        try {
            ResponseEntity<RestUser> response = template.postForEntity(url, entidade, RestUser.class);
            return response.getBody();
        } catch (Exception ignored) {
            System.out.println("Usuário já cadastrado!");
            String publicKeyStr = Base64.getEncoder().encodeToString(kp.getPublic().getEncoded());
            ResponseEntity<RestUser> response = template.postForEntity(
                    String.valueOf(URI.create(url + "/findByChave")),
                    new HttpEntity<>(publicKeyStr, headers),
                    RestUser.class
            );
            return response.getBody();
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    private static class RestUser {

        private int id;
        private byte[] chavePublica;
        private String nome;

    }

}
