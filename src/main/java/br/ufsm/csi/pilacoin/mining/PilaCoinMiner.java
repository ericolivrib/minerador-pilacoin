package br.ufsm.csi.pilacoin.mining;

import br.ufsm.csi.pilacoin.config.PathConfig;
import br.ufsm.csi.pilacoin.service.WebSocketClientService;
import br.ufsm.csi.pilacoin.util.KeysUtil;
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

    private final WebSocketClientService webSocket;
    private BigInteger dificuldade;

    @Autowired
    public PilaCoinMiner(WebSocketClientService webSocket) {
        this.webSocket = webSocket;
    }

    @PostConstruct
    private void init() {
        while (true) {
            dificuldade = webSocket.getDificuldade();

            if (dificuldade != null) {
                new Thread(new Mine(webSocket, dificuldade)).start();
            }
        }
    }

    @SneakyThrows
    private void saveCoin(BigInteger hash) {
        FileWriter fileWriter = new FileWriter(PathConfig.CAMINHO_CRIPTOMOEDAS, true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.newLine();
        bufferedWriter.write(hash.toString());
        bufferedWriter.flush();
    }

    private static class Mine implements Runnable {

        private final WebSocketClientService webSocket;
        private final BigInteger dificuldade;

        public Mine(WebSocketClientService webSocket, BigInteger dificuldade) {
            this.webSocket = webSocket;
            this.dificuldade = dificuldade;
        }

        @Override
        @SneakyThrows
        public void run() {
            BigInteger numHash;

            do {
                Random rnd = new SecureRandom();
                BigInteger magicNumber = new BigInteger(128, rnd).abs();

                Pila coin = Pila.builder()
                        .dataCriacao(new Date())
                        .idCriador("Érico")
                        .nonce(magicNumber.toString())
                        .chaveCriador(KeysUtil.getKey(PathConfig.CAMINHO_PUBLIC_KEY))
                        .build();

                String jsonCoin = new ObjectMapper().writeValueAsString(coin);
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] hash = md.digest(jsonCoin.getBytes(StandardCharsets.UTF_8));
                numHash = new BigInteger(hash).abs();

            } while (Objects.requireNonNull(numHash).compareTo(dificuldade) > 0);

            System.out.println("Novo pila encontrado: " + numHash);

            this.saveCoin(numHash);
        }

        @SneakyThrows
        private void saveCoin(BigInteger hash) {
            System.out.println("Salvando pila...");
            FileWriter fileWriter = new FileWriter(PathConfig.CAMINHO_CRIPTOMOEDAS, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.newLine();
            bufferedWriter.write(hash.toString());
            bufferedWriter.flush();
        }

    }

}
