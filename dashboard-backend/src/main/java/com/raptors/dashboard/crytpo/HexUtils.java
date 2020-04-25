package com.raptors.dashboard.crytpo;

import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Hex;

public class HexUtils {

    private HexUtils() {
    }

    public static String bytesToHex(byte[] bytes) {
        return new String(Hex.encodeHex(bytes));
    }

    @SneakyThrows
    public static byte[] hexToBytes(String hex) {
        return Hex.decodeHex(hex);
    }
}
