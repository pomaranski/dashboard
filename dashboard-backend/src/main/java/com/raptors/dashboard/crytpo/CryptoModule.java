package com.raptors.dashboard.crytpo;

import lombok.SneakyThrows;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;

import static java.nio.charset.StandardCharsets.US_ASCII;

public class CryptoModule {

    private static final String AES_CBC_PKCS_7_PADDING = "AES/CBC/PKCS7Padding";
    private static final String AES = "AES";
    private static final byte[] IV = "1234567891234567".getBytes(US_ASCII);

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private CryptoModule() {
    }

    @SneakyThrows
    public static byte[] encryptAes(byte[] keyBytes, byte[] plaintextBytes) {
        Cipher cipher = Cipher.getInstance(AES_CBC_PKCS_7_PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, getAesKey(keyBytes), new IvParameterSpec(IV));
        return cipher.doFinal(plaintextBytes);
    }

    @SneakyThrows
    public static byte[] decryptAes(byte[] keyBytes, byte[] ciphertext) {
        Cipher cipher = Cipher.getInstance(AES_CBC_PKCS_7_PADDING);
        cipher.init(Cipher.DECRYPT_MODE, getAesKey(keyBytes), new IvParameterSpec(IV));
        return cipher.doFinal(ciphertext);
    }

    private static SecretKey getAesKey(byte[] key) {
        return new SecretKeySpec(key, AES);
    }
}
