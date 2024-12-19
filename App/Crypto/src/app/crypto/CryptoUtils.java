/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.crypto;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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
     * Clase de utilidad para la encriptación y desencriptación de datos utilizando el algoritmo AES.
     * Esta clase proporciona métodos para generar claves a partir de contraseñas, encriptar y desencriptar objetos, cadenas e 
     * enteros, y convertir datos a formato Base64.
     */
public class CryptoUtils {
    private static int keyLength = 32;
    private static String pswd = "1234";
    
    /**
     * Genera una clave secreta AES a partir de una contraseña.
     * 
     * @param password la contraseña utilizada para generar la clave.
     * @return la clave secreta generada.
     */
    private static SecretKey generateKeyFromPassword(String password) {
        byte[] bytes = Arrays.copyOf(password.getBytes(), keyLength);
        SecretKey key = new SecretKeySpec(bytes, "AES");
        return key;
    }
    
    /**
     * Genera una clave secreta AES a partir de una contraseña encriptada.
     * 
     * @param pswdAsBytes la contraseña encriptada representada como un arreglo de bytes.
     * @return la clave secreta generada.
     */    
    private static SecretKey generateKeyFromEncryptedPassword(byte[] pswdAsBytes ) {
        byte[] bytes = Arrays.copyOf(pswdAsBytes, keyLength);
        SecretKey key = new SecretKeySpec(bytes, "AES");
        return key;
    }
    
    /**
     * Encripta un objeto utilizando el algoritmo AES.
     * 
     * @param obj el objeto a encriptar.
     * @param key la clave secreta utilizada para la encriptación.
     * @return el objeto encriptado como un arreglo de bytes.
     */
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
    
    
    /**
     * Serializa un objeto en un array de bytes.
     * 
     * @param obj el objeto a serializar.
     * @return el objeto serializado como un array de bytes.
     * @throws IOException si ocurre un error durante la serialización.
     */
    private static byte[] serializeObject(Object obj) throws IOException {
        System.out.println("Serializing Object");
        if (obj == null) return null;
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        try (ObjectOutputStream objectStream = new ObjectOutputStream(byteStream)) {
            objectStream.writeObject(obj);
        }
        return byteStream.toByteArray();
    }
    
    
    /**
     * Deserializa un array de bytes a un objeto.
     * 
     * @param byteArray el arreglo de bytes que representa el objeto.
     * @return el objeto deserializado.
     * @throws IOException si ocurre un error durante la deserialización.
     * @throws ClassNotFoundException si no se encuentra la clase del objeto.
     */
    private static Object deserializeObject(byte[] byteArray) throws IOException, ClassNotFoundException {
        System.out.println("Deserializing Object");
        if (byteArray == null) return null;
        try (ObjectInputStream objectStream = new ObjectInputStream(new ByteArrayInputStream(byteArray))) {
            return objectStream.readObject();
        }
    }
    
    /**
     * Desencripta un objeto utilizando el algoritmo AES.
     * 
     * @param encryptedObj el objeto encriptado representado como un array de bytes.
     * @param key la clave secreta utilizada para la desencriptación.
     * @return el objeto desencriptado.
     */    
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
    
    /**
     * Encripta una contraseña utilizando el algoritmo AES.
     * 
     * @param pswd la contraseña a encriptar.
     * @return la contraseña encriptada en formato Base64.
     */
    public static String encryptPassword(String pswd) {
        SecretKey key = generateKeyFromPassword(getGenericPassword());
        byte[] data = encryptString(pswd, key);
        String encrypted = byteToB64(data);
        return encrypted;
    }
    
    /**
     * Encripta una cadena de texto utilizando el algoritmo AES.
     * 
     * @param txt la cadena de texto a encriptar.
     * @param key la clave secreta utilizada para la encriptación.
     * @return la cadena de texto encriptada como un arreglo de bytes.
     */
    private static byte[] encryptString(String txt, SecretKey key) {
        System.out.println("encryptString");
        byte[] data = null;
        if (txt == null) return null;
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            data = cipher.doFinal(txt.getBytes("UTF-8"));
        } catch (BadPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | UnsupportedEncodingException ex) {
            throw new CryptoException(ex);
        }
        return data;
    }
    
    /**
     * Desencripta una cadena de texto utilizando el algoritmo AES.
     * 
     * @param data el arreglo de bytes que representa la cadena encriptada.
     * @param key la clave secreta utilizada para la desencriptación.
     * @return la cadena de texto desencriptada.
     */
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
            throw new CryptoException(ex);
        }
        return txt;
    }
    
    /**
     * Convierte un arreglo de bytes a una cadena en formato Base64.
     * 
     * @param data el arreglo de bytes a convertir.
     * @return la cadena en formato Base64.
     */
    private static String byteToB64(byte[] data) {
        try {
            System.out.println("byteToB64. Data: " + data.toString());
            if (data == null) return null;
            String base64 = Base64.getEncoder().encodeToString(data);
            return base64;        
        } catch (Exception ex) {
            throw new CryptoException(ex);
        }
        
    }
    
    /**
     * Convierte una cadena en formato Base64 a un arreglo de bytes.
     * 
     * @param base64 la cadena en formato Base64 a convertir.
     * @return el arreglo de bytes resultante.
     */
    private static byte[] b64ToByte(String base64) {
        try {
            System.out.println("b64ToByte. Data: " + base64);
            if (base64 == null) return null;
            byte[] data = Base64.getDecoder().decode(base64);
            return data;      
        } catch (Exception ex) {
            throw new CryptoException(ex);
        }
    }
    
    /**
     * Envía una cadena de texto encriptada a través de la conexión de red.
     * 
     * @param stream el objeto {@code Stream} utilizado para enviar la cadena.
     * @param txt la cadena de texto a enviar.
     * @param pswd la contraseña utilizada para encriptar la cadena.
     */
    public static void sendString(Stream stream, String txt, String pswd) {
        try {
            
            System.out.println("Sending String");
            
            SecretKey key = generateKeyFromPassword(pswd);
            byte[] data = encryptString(txt, key);
            String b64 = byteToB64(data);
            stream.getWriter().println(b64);        
            
        } catch (Exception ex) {
            
            throw new CryptoException(ex);
        }
    }
    
    /**
     * Lee una cadena de texto encriptada desde la conexión de red y la desencripta.
     * 
     * @param stream el objeto {@code Stream} utilizado para leer la cadena.
     * @param pswd la contraseña utilizada para desencriptar la cadena.
     * @return la cadena de texto desencriptada.
     */
    public static String readString(Stream stream, String pswd)
    {
        System.out.println("readString");
        String txt = null;
        try {
            System.out.println("readString");
            SecretKey key = generateKeyFromPassword(pswd);
            System.out.println("Key generada");
            BufferedReader readFromClient = stream.getReader();
            

            String b64 = readFromClient.readLine();
            byte[] data = b64ToByte(b64);
            System.out.println("Rebut b64: " + b64);
            txt = decryptString(data, key);
            System.out.println("b64 decriptat: " + txt);
            
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new CryptoException(ex);
        }
        return txt;
    }
    
    /**
     * Envía un número entero a través de un flujo encriptado.
     * 
     * @param stream el flujo de salida donde se enviará el entero.
     * @param n el número entero a enviar.
     * @param pswd la contraseña para generar la clave de cifrado.
     * @throws CryptoException si ocurre un error al enviar el número.
     */
    public static void sendInt(Stream stream, int n, String pswd) {
        try {
            System.out.println("Sending Int");
            String nm = Integer.toString(n);
            sendString(stream, nm, pswd);    
        } catch (Exception ex) {
            throw new CryptoException(ex);
        }
        
    }
    
    /**
     * Lee un número entero desde un flujo encriptado.
     * 
     * @param stream el flujo de entrada desde el cual se leerá el entero.
     * @param pswd la contraseña para generar la clave de cifrado.
     * @return el número entero leído, o {@code null} si no se encuentra ningún valor.
     * @throws CryptoException si ocurre un error al leer el número.
     */
    public static Integer readInt(Stream stream, String pswd) {
        try {
            System.out.println("Reading Int");
            String nm = readString(stream, pswd);
            if (nm == null) return null;
            int n = Integer.parseInt(nm);
            return n;  
        } catch (Exception ex) {
            throw new CryptoException(ex);
        }
    }
    
    
    /**
     * Envía un objeto a través de un flujo encriptado.
     * 
     * @param stream el flujo de salida donde se enviará el objeto.
     * @param obj el objeto a enviar.
     * @param pswd la contraseña para generar la clave de cifrado.
     * @throws CryptoException si ocurre un error al enviar el objeto.
     */
    public static void sendObject(Stream stream, Object obj, String pswd) {
         try {
            System.out.println("Sending object");
            ObjectOutputStream out = stream.getOut();
            SecretKey key = generateKeyFromPassword(pswd);
            byte[] data = encryptObject(obj, key);
            out.writeObject((Object)data);
            out.flush();
              
            System.out.println("Object sent");
        } catch (Exception ex) {
            throw new CryptoException(ex);
        }
    }
    
    /**
     * Lee un objeto desde un flujo encriptado.
     * 
     * @param stream el flujo de entrada desde el cual se leerá el objeto.
     * @param pswd la contraseña para generar la clave de cifrado.
     * @return el objeto leído.
     * @throws CryptoException si ocurre un error al leer el objeto.
     */
    public static Object readObject(Stream stream, String pswd) {
        System.out.println("Reading object");
        try {
            ObjectInputStream in = stream.getIn();
            SecretKey key = generateKeyFromPassword(pswd);
            byte[] data = (byte[]) in.readObject();
            System.out.println("readObject: data read");
            System.out.println(data);
            Object obj = decryptObject(data, key);
            System.out.println("Object read");
            return obj;
        } catch (Exception ex) {
            throw new CryptoException(ex);
        }
        
        
    }
    
    /**
     * Obtiene la contraseña genérica usada para la encriptación.
     * 
     * @return la contraseña genérica.
     */
    public static String getGenericPassword() {
        return pswd;
    }
}
