/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.servidor;

/**
 *
 * @author Sergio
 */
public class ServidorException extends RuntimeException {

    public ServidorException(String message) {
        super(message);
    }

    public ServidorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServidorException(Throwable cause) {
        super(cause);
    }

    public ServidorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
    
}
