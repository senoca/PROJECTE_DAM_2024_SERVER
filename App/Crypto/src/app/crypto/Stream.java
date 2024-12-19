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
 *
 * @author Sergio
 */
public class Stream {
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;
    private BufferedReader reader = null;
    private PrintWriter writer = null;

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

    public ObjectInputStream getIn() {
        return in;
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public PrintWriter getWriter() {
        return writer;
    }
    
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
