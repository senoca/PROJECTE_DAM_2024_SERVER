/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.crypto;

/**
 * Excepción personalizada para manejar errores relacionados con operaciones criptográficas.
 * Extiende {@link RuntimeException} y se utiliza para capturar y manejar errores específicos de criptografía.
 * 
 * @author Sergio
 */
public class CryptoException extends RuntimeException {

    /**
     * Crea una nueva instancia de CryptoException con un mensaje de error.
     * 
     * @param message El mensaje de error asociado con la excepción.
     */
    public CryptoException(String message) {
        super(message);
    }

    /**
     * Crea una nueva instancia de CryptoException con un mensaje de error y la causa de la excepción.
     * 
     * @param message El mensaje de error asociado con la excepción.
     * @param cause La causa de la excepción, generalmente otra excepción que originó el error.
     */
    public CryptoException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Crea una nueva instancia de CryptoException con una excepción original.
     * 
     * @param ex La excepción original que causó el error criptográfico.
     */
    CryptoException(Exception ex) {
        super(ex);
    }
    
}
