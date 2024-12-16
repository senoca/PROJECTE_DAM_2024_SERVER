/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.crypto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Sergio
 */
public class CryptoUtils {
    private static int keyLength = 32;
    
    
    public static SecretKey generateKeyFromPassword(String password) {
        byte[] bytes = Arrays.copyOf(password.getBytes(), keyLength);
        SecretKey key = new SecretKeySpec(bytes, "AES");
        return key;
    }
    
    
    public static SecretKey generateKeyFromEncryptedPassword(byte[] pswdAsBytes ) {
        byte[] bytes = Arrays.copyOf(pswdAsBytes, keyLength);
        SecretKey key = new SecretKeySpec(bytes, "AES");
        return key;
    }
    
    public static byte[] encryptObject(Object obj, SecretKey key) {
        byte[] encryptedObj = null;
        try {
            byte[] serializedObj = serializeObject(obj);
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            encryptedObj = cipher.doFinal(serializedObj);
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException| NoSuchPaddingException | NoSuchAlgorithmException | IOException ex) {
            throw new CryptoException(ex.getMessage());
        }
        return encryptedObj;
    }
    
    
    private static byte[] serializeObject(Object obj) throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try (ObjectOutputStream objectStream = new ObjectOutputStream(byteStream)) {
            objectStream.writeObject(obj);
        }
        return byteStream.toByteArray();
    }
    
    private static Object deserializeObject(byte[] byteArray) throws IOException, ClassNotFoundException {
        try (ObjectInputStream objectStream = new ObjectInputStream(new ByteArrayInputStream(byteArray))) {
            return objectStream.readObject();
        }
    }
    
        
    public static Object decryptObject(byte[] encryptedObj, SecretKey key) {
        Object decryptedObj = null;
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] serializedObj = cipher.doFinal(encryptedObj);
            decryptedObj = deserializeObject(serializedObj);
        } catch (ClassNotFoundException | BadPaddingException | IllegalBlockSizeException | InvalidKeyException| NoSuchPaddingException | NoSuchAlgorithmException | IOException ex) {
            throw new CryptoException(ex.getMessage());
        }
        return decryptedObj;
    }
    
    public static byte[] encryptString(String txt, SecretKey key) {
        byte[] encryptedTxt = null;
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            encryptedTxt = cipher.doFinal(txt.getBytes("UTF-8"));
        } catch (BadPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | UnsupportedEncodingException ex) {
            throw new CryptoException(ex.getMessage());
        }
        return encryptedTxt;
    }
    
    public static String decryptString(byte[] encryptedTxt, SecretKey key) {
        String txt = null;
        try {    
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptedBytes = cipher.doFinal(encryptedTxt);
            txt = new String(decryptedBytes, "UTF-8");
        } catch (NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException ex) {
            throw new CryptoException(ex.getMessage());
        }
        return txt;
    }
}
