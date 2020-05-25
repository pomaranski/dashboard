package com.raptors.dashboard.crytpo;

import lombok.SneakyThrows;
import org.junit.Assert;
import org.junit.Test;

import javax.crypto.Cipher;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import static java.nio.charset.StandardCharsets.US_ASCII;

public class CryptoModuleTest {

    @Test
    public void encryptAndDecryptAes() {
        String key = "645813e7988e88827c62148a27b1624f";
        String data = "data";

        byte[] encrypted = CryptoModule.encryptAes(HexUtils.hexToBytes(key), data.getBytes(US_ASCII));

        Assert.assertEquals("979d28cb2b80266e6d3199f5f0f3e27e", HexUtils.bytesToHex(encrypted));

        byte[] decrypted = CryptoModule.decryptAes(HexUtils.hexToBytes(key), encrypted);

        Assert.assertEquals(data, new String(decrypted, US_ASCII));
    }

    @Test
    public void encryptAndDecryptRsa() throws NoSuchAlgorithmException {
        KeyPair key = getKeyPair();
        String data = "data";

        byte[] encrypted = CryptoModule.encryptRsa(key.getPublic().getEncoded(), data.getBytes(US_ASCII));

        byte[] decrypted = decrypt(key, encrypted);

        Assert.assertEquals(data, new String(decrypted, US_ASCII));
    }

    @SneakyThrows
    private byte[] decrypt(KeyPair key, byte[] encrypted) {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        cipher.init(Cipher.DECRYPT_MODE, key.getPrivate());
        return cipher.doFinal(encrypted);
    }

    private KeyPair getKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024);
        return keyGen.generateKeyPair();
    }

}
