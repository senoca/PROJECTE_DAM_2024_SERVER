/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.crypto;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
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
    private static String pswd = "1234";
    
    private static SecretKey generateKeyFromPassword(String password) {
        byte[] bytes = Arrays.copyOf(password.getBytes(), keyLength);
        SecretKey key = new SecretKeySpec(bytes, "AES");
        return key;
    }
    
    
    private static SecretKey generateKeyFromEncryptedPassword(byte[] pswdAsBytes ) {
        byte[] bytes = Arrays.copyOf(pswdAsBytes, keyLength);
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
            throw new CryptoException(ex);
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
            throw new CryptoException(ex);
        }
        return decryptedObj;
    }
    
    private static byte[] encryptString(String txt, SecretKey key) {
        System.out.println("encryptString");
        byte[] data = null;
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            data = cipher.doFinal(txt.getBytes("UTF-8"));
        } catch (BadPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | UnsupportedEncodingException ex) {
            throw new CryptoException(ex);
        }
        return data;
    }
    
    private static String decryptString(byte[] data, SecretKey key) {
        System.out.println("Decrypting String");
        String txt = null;
        try {    
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decryptedBytes = cipher.doFinal(data);
            txt = new String(decryptedBytes, "UTF-8");
        } catch (NoSuchPaddingException | BadPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException ex) {
            throw new CryptoException(ex);
        }
        return txt;
    }
    
    private static String byteToB64(byte[] data) {
        try {
            System.out.println("byteToB64. Data: " + data.toString());
            String base64 = Base64.getEncoder().encodeToString(data);
            return base64;        
        } catch (Exception ex) {
            throw new CryptoException(ex);
        }
        
    }
    
    private static byte[] b64ToByte(String base64) {
        try {
            System.out.println("b64ToByte. Data: " + base64);
            byte[] data = Base64.getDecoder().decode(base64);
            return data;      
        } catch (Exception ex) {
            throw new CryptoException(ex);
        }
        
        
    }
    
    public static void sendString(OutputStream out, String txt, String pswd) throws IOException {
        System.out.println("Sending String");
        PrintWriter writeToServer = new PrintWriter(out, true);    
        
            SecretKey key = generateKeyFromPassword(pswd);
            byte[] data = encryptString(txt, key);
            String b64 = byteToB64(data);
            writeToServer.println(b64);    
        
            
    }
    
    public static String readString(InputStream in, String pswd) throws IOException {
        System.out.println("readString");
        SecretKey key = generateKeyFromPassword(pswd);
        BufferedReader readFromClient = new BufferedReader(new InputStreamReader(in));
        String txt = null;
        
            String b64 = readFromClient.readLine();
            byte[] data = b64ToByte(b64);
            System.out.println("Rebut b64: " + b64);
            txt = decryptString(data, key);
            System.out.println("b64 decriptat: " + txt);
        return txt;
    }
    
    public static void sendInt(OutputStream out, int n, String pswd) throws IOException {
        System.out.println("Sending Int");
        String nm = Integer.toString(n);
        sendString(out, nm, pswd);
    }
    
    public static int readInt(InputStream in, String pswd) throws IOException {
        System.out.println("Reading Int");
        String nm = readString(in, pswd);
        int n = Integer.parseInt(nm);
        return n;
        
    }
    
    public static void sendObject(OutputStream out, Object obj, String pswd) throws IOException {
         try {
            System.out.println("Sending object");
            ObjectOutputStream stream = new ObjectOutputStream(out);
            SecretKey key = generateKeyFromPassword(pswd);
            byte[] data = encryptObject(obj, key);
            stream.write(data);
            stream.flush();
            System.out.println("Object sent");
        } catch (Exception ex) {
            throw new CryptoException(ex);
        }
        
    }
    
    public static Object readObject(InputStream in, String pswd) throws IOException {
        System.out.println("Reading object");
        try {
            ObjectInputStream stream = new ObjectInputStream(in);
            SecretKey key = generateKeyFromPassword(pswd);
            System.out.println("readObject: ready to read data");
            byte[] data = stream.readAllBytes();
            System.out.println("readObject: data read");
            System.out.println(data);
            Object obj = decryptObject(data, key);
            System.out.println("Object read");
            return obj;
        } catch (Exception ex) {
            throw new CryptoException(ex);
        }
        
        
    }
    
    public static String getGenericPassword() {
        return pswd;
    }
}
