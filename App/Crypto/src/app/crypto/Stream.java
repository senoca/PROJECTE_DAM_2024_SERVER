/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.crypto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase que maneja la entrada y salida de datos a través de un socket.
 * Proporciona métodos para leer y escribir datos en el socket, tanto en formato de objetos como en formato de texto.
 * También incluye un método para cerrar correctamente las conexiones.
 * 
 * @author Sergio
 */
public class Stream {
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;
    private BufferedReader reader = null;
    private PrintWriter writer = null;

    /**
     * Constructor que inicializa los flujos de entrada y salida asociados con un socket.
     * 
     * @param soc El socket a través del cual se establecerán las conexiones de entrada y salida.
     * @throws CryptoException Si ocurre un error al crear los flujos de entrada o salida.
     */
    public Stream(Socket soc) {
        System.out.println("Creant stream...");
        try {
            out = new ObjectOutputStream(soc.getOutputStream());
            System.out.println("out iniciat");
            reader = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            System.out.println("reader iniciat");
            writer = new PrintWriter(soc.getOutputStream(), true);
            System.out.println("writer iniciat");
            in = new ObjectInputStream(soc.getInputStream());
            System.out.println("in iniciat");
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new CryptoException(ex);
        }
    }

    /**
     * Obtiene el flujo de entrada de objetos.
     * 
     * @return El flujo de entrada de objetos.
     */
    public ObjectInputStream getIn() {
        return in;
    }

    /**
     * Obtiene el flujo de salida de objetos.
     * 
     * @return El flujo de salida de objetos.
     */
    public ObjectOutputStream getOut() {
        return out;
    }

    /**
     * Obtiene el flujo de entrada de texto.
     * 
     * @return El flujo de entrada de texto.
     */
    public BufferedReader getReader() {
        return reader;
    }

    /**
     * Obtiene el flujo de salida de texto.
     * 
     * @return El flujo de salida de texto.
     */
    public PrintWriter getWriter() {
        return writer;
    }
    
    /**
     * Cierra todos los flujos asociados con el socket (entrada y salida de objetos y texto).
     * 
     */
    public void close() {
        try {
            in.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            reader.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            writer.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("Stream closed");
    }
}
