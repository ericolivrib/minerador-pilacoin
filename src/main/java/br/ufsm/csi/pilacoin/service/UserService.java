package br.ufsm.csi.pilacoin.service;

import br.ufsm.csi.pilacoin.model.Usuario;
import br.ufsm.csi.pilacoin.repository.UserRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Base64;

@Service
public class UserService extends SwaggerService {

    String url = "http://" + serverAddress + "/usuario/";

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @SneakyThrows
    @Transactional
    public Usuario registrar(Usuario usuario) {
        HttpEntity<Usuario> entidade = new HttpEntity<>(usuario, headers);

        try {
            ResponseEntity<Usuario> response = template.postForEntity(url, entidade, Usuario.class);
            return response.getBody();
        } catch (Exception ignored) {
            System.out.println("Usuário já cadastrado!");
            String publicKeyStr = Base64.getEncoder().encodeToString(usuario.getChavePublica());
            ResponseEntity<Usuario> response = template.postForEntity(
                    url + "/findByChave",
                    new HttpEntity<>(publicKeyStr, headers),
                    Usuario.class
            );
            return response.getBody();
        }
    }

}
