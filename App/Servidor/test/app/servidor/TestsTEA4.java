/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package app.servidor;

import app.crypto.CryptoException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.jupiter.api.Assertions;

/**
 *
 * @author Sergio
 */
public class TestsTEA4 {
    
    public TestsTEA4() {
    }
    
    /*
    Aquest test comprova que funcioni el métode d'encriptació d'strings
    */
    @Test
    public void testCryptoString() {
        String txt = "Hola món!";
        String seed = "seed";
        SecretKey key = generateKeyFromPassword(seed);
        
        byte[] data = encryptString(txt, key);
        System.out.println("String com a byte array: " + data.toString());
        
        String decrypted = decryptString(data, key);
        
        Assertions.assertTrue(txt.equals(decrypted));
    } 
    
    /*
    Aquest test comprova que funcioni el métode que converteix byte[] a base64
    */
    @Test
    public void testBase64() {
        String txt = "Hola món!";
        String seed = "seed";
        SecretKey key = generateKeyFromPassword(seed);
        
        byte[] data = encryptString(txt, key);
        String b64 = byteToB64(data);
        System.out.println("String com a Base64: " + b64);
        data = b64ToByte(b64);
        String decrypted = decryptString(data, key);
        
        Assertions.assertTrue(txt.equals(decrypted));
    }
    
    /*
    Aquest test comprova que funcioni el métode d'encriptació d'objectes
    */
    @Test
    public void testCryptoObject() {
        ArrayList<Integer> a = new ArrayList<Integer>();
        a.add(1);
        a.add(2);
        a.add(3);
        
        String seed = "seed";
        SecretKey key = generateKeyFromPassword(seed);
        byte[] data = encryptObject(a, key);
        System.out.println("Llista encriptada: " + data.toString());
        ArrayList<Integer> b = null;
        b = (ArrayList<Integer>) decryptObject(data, key);
        Assertions.assertTrue(a.equals(b));
    }
    
    /*
    Aquest test comprova que funcioni el métode d'encriptació d'ints
    */
    @Test
    public void testCryptoInt() {
        int n = 47;
        String seed = "seed";
        SecretKey key = generateKeyFromPassword(seed);
        byte[] data = encryptInt(n, key);
        System.out.println("Int encriptat: " + data.toString());
        int m = decryptInt(data, key);
        Assertions.assertTrue(n == m);
        
    }
    
    private static int keyLength = 32;
    private static String pswd = "1234";
    
    
    private static byte[] encryptInt(int n, SecretKey key) {
        String nm = Integer.toString(n);
        return encryptString(nm, key);
    }
    
    private static int decryptInt(byte[] data, SecretKey key) {
        String nm = decryptString(data, key);
        return Integer.parseInt(nm);
    }
    
    private static SecretKey generateKeyFromPassword(String password) {
        byte[] bytes = Arrays.copyOf(password.getBytes(), keyLength);
        SecretKey key = new SecretKeySpec(bytes, "AES");
        return key;
    }
    

    private static byte[] encryptObject(Object obj, SecretKey key) {
        System.out.println("Encrypting object");
        byte[] encryptedObj = null;
        try {
            byte[] serializedObj = serializeObject(obj);
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            encryptedObj = cipher.doFinal(serializedObj);
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException| NoSuchPaddingException | NoSuchAlgorithmException | IOException ex) {
            throw new RuntimeException(ex);
        }
        return encryptedObj;
    }
    
    private static byte[] serializeObject(Object obj) throws IOException {
        System.out.println("Serializing Object");
        if (obj == null) return null;
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try (ObjectOutputStream objectStream = new ObjectOutputStream(byteStream)) {
            objectStream.writeObject(obj);
        }
        return byteStream.toByteArray();
    }
    
    private static Object deserializeObject(byte[] byteArray) throws IOException, ClassNotFoundException {
        System.out.println("Deserializing Object");
        if (byteArray == null) return null;
        try (ObjectInputStream objectStream = new ObjectInputStream(new ByteArrayInputStream(byteArray))) {
            return objectStream.readObject();
        }
    }
    
        
    private static Object decryptObject(byte[] encryptedObj, SecretKey key) {
        System.out.println("Decrypting Object");
        Object decryptedObj = null;
        if (encryptedObj == null) return null;
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] serializedObj = cipher.doFinal(encryptedObj);
            decryptedObj = deserializeObject(serializedObj);
        } catch (ClassNotFoundException | BadPaddingException | IllegalBlockSizeException | InvalidKeyException| NoSuchPaddingException | NoSuchAlgorithmException | IOException ex) {
            throw new RuntimeException(ex);
        }
        return decryptedObj;
    }
    
    private static String byteToB64(byte[] data) {
        try {
            System.out.println("byteToB64. Data: " + data.toString());
            if (data == null) return null;
            String base64 = Base64.getEncoder().encodeToString(data);
            return base64;        
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        
    }
    
    private static byte[] b64ToByte(String base64) {
        try {
            System.out.println("b64ToByte. Data: " + base64);
            if (base64 == null) return null;
            byte[] data = Base64.getDecoder().decode(base64);
            return data;      
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
    private static byte[] encryptString(String txt, SecretKey key) {
        System.out.println("encryptString");
        byte[] data = null;
        if (txt == null) return null;
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            data = cipher.doFinal(txt.getBytes("UTF-8"));
        } catch (BadPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
        return data;
    }
    
    private static String decryptString(byte[] data, SecretKey key) {
        System.out.println("Decrypting String");
        String txt = null;
        if (data == null) return null;
        try {    
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptedBytes = cipher.doFinal(data);
            txt = new String(decryptedBytes, "UTF-8");
        } catch (NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException ex) {
            throw new RuntimeException(ex);
        }
        return txt;
    }
    
    
    
}
