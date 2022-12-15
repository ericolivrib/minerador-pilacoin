package br.ufsm.csi.pilacoin.util;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.FileReader;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public final class KeysPairUtil {

    @SneakyThrows
    public static byte[] getKey(String caminho) {
        FileReader fileReader = new FileReader(caminho);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        String keyString = bufferedReader.readLine();

        fileReader.close();
        bufferedReader.close();

        String[] bytes = keyString.split(", ");
        byte[] keyBytes = new byte[bytes.length];

        for (int i = 0; i < bytes.length; i++) {
            int b = Integer.parseInt(bytes[i]);
            keyBytes[i] = (byte) b;
        }

        return keyBytes;
    }

    @SneakyThrows
    private static PublicKey convertToPublicKey(byte[] keyBytes) {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(keyBytes));
        return publicKey;
    }

    @SneakyThrows
    private static PrivateKey convertToPrivateKey(byte[] keyBytes) {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(keyBytes));
        return privateKey;
    }

    @SneakyThrows
    public static KeyPair getKeyPair() {
        byte[] bytePublic = getKey("src/main/resources/arquivos/public-key.txt");
        byte[] bytePrivate = getKey("src/main/resources/arquivos/private-key.txt");

        PublicKey publicKey = convertToPublicKey(bytePublic);
        PrivateKey privateKey = convertToPrivateKey(bytePrivate);

        return new KeyPair(publicKey, privateKey);
    }

}
