package br.ufsm.csi.pilacoin.mining;

import br.ufsm.csi.pilacoin.model.PilaCoin;
import br.ufsm.csi.pilacoin.service.PilaCoinService;
import br.ufsm.csi.pilacoin.service.WebSocketClientService;
import br.ufsm.csi.pilacoin.util.KeysPairUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

@Service
public class PilaCoinMiner {

    private final PilaCoinService pilaCoinService;
    private final WebSocketClientService webSocket;
    private BigInteger dificuldade;

    @Autowired
    public PilaCoinMiner(WebSocketClientService webSocket, PilaCoinService pilaCoinService) {
        this.webSocket = webSocket;
        this.pilaCoinService = pilaCoinService;
    }

    @PostConstruct
    private void init() {
        while (true) {
            dificuldade = webSocket.getDificuldade();

            if (dificuldade != null) {
                minerar();
            }
        }
    }

    @SneakyThrows
    public void minerar() {
        BigInteger numHash;
        PilaCoin pilaCoin;

        do {
            Random rnd = new SecureRandom();
            BigInteger magicNumber = new BigInteger(128, rnd).abs();
            byte[] chave = KeysPairUtil.getKey("src/main/resources/arquivos/public-key.txt");

            pilaCoin = new PilaCoin();
            pilaCoin.setDataCriacao(new Date());
            pilaCoin.setNonce(magicNumber.toString());
            pilaCoin.setChaveCriador(chave);

            String jsonCoin = new ObjectMapper().writeValueAsString(pilaCoin);
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(jsonCoin.getBytes(StandardCharsets.UTF_8));
            numHash = new BigInteger(hash).abs();

        } while (Objects.requireNonNull(numHash).compareTo(dificuldade) > 0);

        System.out.println("Novo pila encontrado: " + numHash);

        saveCoin(numHash);
        pilaCoinService.enviar(pilaCoin);
    }

    @SneakyThrows
    private void saveCoin(BigInteger hash) {
        FileWriter fileWriter = new FileWriter("src/main/resources/arquivos/coins.txt", true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.newLine();
        bufferedWriter.write(hash.toString());
        bufferedWriter.flush();
    }

}
