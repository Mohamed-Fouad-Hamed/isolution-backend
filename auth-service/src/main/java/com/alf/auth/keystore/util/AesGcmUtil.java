package com.alf.auth.keystore.util;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;


public class AesGcmUtil {
    private static final int IV_LEN = 12; // 96 bits
    private static final int TAG_LEN = 128; // bits


    // encryptionKey: raw 32 bytes
    public static String encrypt(byte[] encryptionKey, byte[] plain) throws Exception {
        byte[] iv = new byte[IV_LEN];
        new SecureRandom().nextBytes(iv);


        SecretKey key = new SecretKeySpec(encryptionKey, "AES");
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(TAG_LEN, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);
        byte[] cipherText = cipher.doFinal(plain);


        byte[] out = new byte[iv.length + cipherText.length];
        System.arraycopy(iv, 0, out, 0, iv.length);
        System.arraycopy(cipherText, 0, out, iv.length, cipherText.length);
        return Base64.getEncoder().encodeToString(out);
    }


    public static byte[] decrypt(byte[] encryptionKey, String base64Cipher) throws Exception {
        byte[] all = Base64.getDecoder().decode(base64Cipher);
        byte[] iv = new byte[IV_LEN];
        System.arraycopy(all, 0, iv, 0, IV_LEN);
        byte[] cipherText = new byte[all.length - IV_LEN];
        System.arraycopy(all, IV_LEN, cipherText, 0, cipherText.length);


        SecretKey key = new SecretKeySpec(encryptionKey, "AES");
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(TAG_LEN, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, spec);
        return cipher.doFinal(cipherText);
    }
}
