package com.raptors.dashboard.crytpo;

import org.junit.Assert;
import org.junit.Test;

import java.net.URI;

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
    public void test() {
        String address = "http://localhost:8080";
        String data = "data";

        Assert.assertEquals("localhost", URI.create(address).getHost());
        Assert.assertEquals(8080, URI.create(address).getPort());
        Assert.assertEquals("http", URI.create(address).getScheme());
    }

}