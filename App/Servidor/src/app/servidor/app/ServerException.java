/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.servidor.app;

/**
 * Classe per generar les excepcions relacionades amb el servidor.
 * Aquesta classe estèn la classe RuntimeException per a gestionar excepcions en temps d'execució
 * específiques del servidor.
 * @author Sergio
 */
public class ServerException extends RuntimeException {

    /**
     * Constructor per crear una excepció amb un missatge.
     * @param message El missatge d'error associat amb l'excepció.
     */
    public ServerException(String message) {
        super(message);
    }

    /**
     * Constructor per crear una excepció amb un missatge i una causa.
     * @param message El missatge d'error associat amb l'excepció.
     * @param cause L'error o excepció original que va causar aquesta excepció.
     */
    public ServerException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor per crear una excepció a partir d'una altra excepció.
     * @param cause L'error o excepció original que va causar aquesta excepció.
     */
    public ServerException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructor per crear una excepció amb un missatge, una causa i opcions sobre la supressió
     * i la traça de la pila.
     * @param message El missatge d'error associat amb l'excepció.
     * @param cause L'error o excepció original que va causar aquesta excepció.
     * @param enableSuppression Determina si s'ha de permetre la supressió d'excepcions.
     * @param writableStackTrace Determina si la traça de la pila pot ser escrita.
     */
    public ServerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
    
}
