package com.raptors.dashboard.crytpo;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class HexUtils {

    private HexUtils() {
    }

    public static String bytesToHex(byte[] bytes) {
        return new String(Hex.encodeHex(bytes));
    }

    public static byte[] hexToBytes(String hex) throws DecoderException {
        return Hex.decodeHex(hex);
    }
}
