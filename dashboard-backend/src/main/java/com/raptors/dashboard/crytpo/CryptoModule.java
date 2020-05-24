package com.raptors.dashboard.crytpo;

import lombok.SneakyThrows;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.X509EncodedKeySpec;

import static java.nio.charset.StandardCharsets.US_ASCII;

public class CryptoModule {

    private static final String AES_CBC_PKCS_7_PADDING = "AES/CBC/PKCS7Padding";
    private static final String RSA_ECB_OAEPWITH_SHA_256_AND_MGF_1_PADDING = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";
    private static final String AES = "AES";
    private static final String RSA = "RSA";
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

    @SneakyThrows
    public static byte[] encryptRsa(byte[] keyBytes, byte[] plaintextBytes) {
        Cipher cipher = Cipher.getInstance(RSA_ECB_OAEPWITH_SHA_256_AND_MGF_1_PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, getRsaPublicKey(keyBytes));
        return cipher.doFinal(plaintextBytes);
    }

    private static SecretKey getAesKey(byte[] key) {
        return new SecretKeySpec(key, AES);
    }

    @SneakyThrows
    private static PublicKey getRsaPublicKey(byte[] key) {
        return KeyFactory.getInstance(RSA).generatePublic(new X509EncodedKeySpec(key));
    }
}
