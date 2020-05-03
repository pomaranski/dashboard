package com.raptors.dashboard.crytpo;

import com.raptors.dashboard.security.SecurityPropertyHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static com.raptors.dashboard.crytpo.HexUtils.bytesToHex;
import static com.raptors.dashboard.crytpo.HexUtils.hexToBytes;
import static java.nio.charset.StandardCharsets.US_ASCII;

@Component
public class CryptoProvider {

    private final SecurityPropertyHolder securityPropertyHolder;

    public CryptoProvider(SecurityPropertyHolder securityPropertyHolder) {
        this.securityPropertyHolder = securityPropertyHolder;
    }

    public String encryptAes(String encryptedKey, String data) {
        byte[] plainKey = CryptoModule.decryptAes(hexToBytes(securityPropertyHolder.getKek()), hexToBytes(encryptedKey));
        String encrypted = bytesToHex(CryptoModule.encryptAes(plainKey, data.getBytes(US_ASCII)));
        clearKey(plainKey);
        return encrypted;
    }

    public String decryptAes(String encryptedKey, String data) {
        byte[] plainKey = CryptoModule.decryptAes(hexToBytes(securityPropertyHolder.getKek()), hexToBytes(encryptedKey));
        String decrypted = new String(CryptoModule.decryptAes(plainKey, hexToBytes(data)), US_ASCII);
        clearKey(plainKey);
        return decrypted;
    }

    public String encryptRsa(String hexKey, String data) {
        return bytesToHex(CryptoModule.encryptRsa(hexToBytes(hexKey), data.getBytes(US_ASCII)));
    }

    private void clearKey(byte[] key) {
        Arrays.fill(key, (byte) 0);
    }
}
