package com.raptors.dashboard.crytpo;

import com.raptors.dashboard.security.SecurityPropertyHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static com.raptors.dashboard.crytpo.CryptoModule.decryptAes;
import static com.raptors.dashboard.crytpo.CryptoModule.encryptAes;
import static com.raptors.dashboard.crytpo.HexUtils.bytesToHex;
import static com.raptors.dashboard.crytpo.HexUtils.hexToBytes;
import static java.nio.charset.StandardCharsets.US_ASCII;

@Component
public class CryptoProvider {

    private final SecurityPropertyHolder securityPropertyHolder;

    public CryptoProvider(SecurityPropertyHolder securityPropertyHolder) {
        this.securityPropertyHolder = securityPropertyHolder;
    }

    public String encrypt(String encryptedKey, String data) {
        byte[] plainKey = decryptAes(hexToBytes(securityPropertyHolder.getKek()), hexToBytes(encryptedKey));
        String encrypted = bytesToHex(encryptAes(plainKey, data.getBytes(US_ASCII)));
        clearKey(plainKey);
        return encrypted;
    }

    public String decrypt(String encryptedKey, String data) {
        byte[] plainKey = decryptAes(hexToBytes(securityPropertyHolder.getKek()), hexToBytes(encryptedKey));
        String decrypted = new String(decryptAes(plainKey, hexToBytes(data)), US_ASCII);
        clearKey(plainKey);
        return decrypted;
    }

    private void clearKey(byte[] key) {
        Arrays.fill(key, (byte) 0);
    }
}
