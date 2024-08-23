package dev.jlkeesh.utils;

import dev.jlkeesh.exception.ServiceException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AESUtil {

    private static final String salt = "SaltSalt";
    private static final int IV_LENGTH = 16;

    private static byte[] getSaltBytes() throws Exception {
        return salt.getBytes(StandardCharsets.UTF_8);
    }

    private static char[] getMasterPassword() {
        return "SuperSecretPassword".toCharArray();
    }

    public static String encrypt(String input) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            PBEKeySpec spec = new PBEKeySpec(getMasterPassword(), getSaltBytes(), 256, 256);
            SecretKey secretKey = factory.generateSecret(spec);
            SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secret);
            byte[] ivBytes = cipher.getParameters().getParameterSpec(IvParameterSpec.class).getIV();
            byte[] encryptedTextBytes = cipher.doFinal(input.getBytes("UTF-8"));
            byte[] finalByteArray = new byte[ivBytes.length + encryptedTextBytes.length];
            System.arraycopy(ivBytes, 0, finalByteArray, 0, ivBytes.length);
            System.arraycopy(encryptedTextBytes, 0, finalByteArray, ivBytes.length, encryptedTextBytes.length);
            return Base64.getUrlEncoder().encodeToString(finalByteArray);
        } catch (Exception e) {
            throw new ServiceException("internal service error", 500);
        }
    }

    public static String decrypt(String input) {
        try {
            if (input.length() <= IV_LENGTH) {
                throw new Exception("The input string is not long enough to contain the initialisation bytes and data.");
            }
            byte[] byteArray = Base64.getUrlDecoder().decode(input);
            byte[] ivBytes = new byte[IV_LENGTH];
            System.arraycopy(byteArray, 0, ivBytes, 0, 16);
            byte[] encryptedTextBytes = new byte[byteArray.length - ivBytes.length];
            System.arraycopy(byteArray, IV_LENGTH, encryptedTextBytes, 0, encryptedTextBytes.length);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            PBEKeySpec spec = new PBEKeySpec(getMasterPassword(), getSaltBytes(), 256, 256);
            SecretKey secretKey = factory.generateSecret(spec);
            SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(ivBytes));
            byte[] decryptedTextBytes = cipher.doFinal(encryptedTextBytes);
            return new String(decryptedTextBytes);
        } catch (Exception e) {
            throw new ServiceException("internal service error", 500);
        }
    }

}
