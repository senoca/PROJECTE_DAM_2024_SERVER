/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package app.servidor;

import app.model.Author;
import app.servidor.app.Utils;
import app.model.User;
import app.servidor.app.ServerException;
import app.servidor.entity.DBAuthor;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.Assert;
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
public class TestsTEA3 {

    
    
    /**
     * Aquest test comprobarà que el nombre d'autors a la bd és igual al que retorna GetAuthors(), demostrant la comunicació per sockets.
     */
    @Test
    public void GetTotalAuthors() throws IOException, ClassNotFoundException, InterruptedException
    {   
        int total = 4;
            int port = 12345;
            InetAddress ip = InetAddress.getLocalHost();
            Socket soc = new Socket(ip, port);
            PrintWriter writeToServer = new PrintWriter(soc.getOutputStream(), true);
            writeToServer.println("GET_ALL_AUTHORS");
            ObjectInputStream objectInput = new ObjectInputStream(soc.getInputStream());
            ArrayList<Author> authors = null;
            authors = (ArrayList<Author>) objectInput.readObject();
            soc.close();
            Assertions.assertEquals(total, authors.size());
    }
    
    /**
     * Aquest test comprobarà que el nombre d'autors a la bd és igual al que retorna GetAuthors(), demostrant la comunicació per sockets.
     */
    @Test
    public void GetNameAuthors() throws IOException, ClassNotFoundException, InterruptedException
    {   
        String[] authorNames = {"Sófocles", "Virgilio", "John Ronald", "Lope"};
        String[] authorSurname = {null, null, "Tolkien", "De Vega"};
            int port = 12345;
            InetAddress ip = InetAddress.getLocalHost();
            Socket soc = new Socket(ip, port);
            PrintWriter writeToServer = new PrintWriter(soc.getOutputStream(), true);
            writeToServer.println("GET_ALL_AUTHORS");
            ObjectInputStream objectInput = new ObjectInputStream(soc.getInputStream());
            ArrayList<Author> authors = null;
            authors = (ArrayList<Author>) objectInput.readObject();
            soc.close();
            for(int i = 0; i < 4; i++) {
                Assertions.assertTrue(authors.get(i).getAuthorname().equals(authorNames[i]));
                if (authors.get(i).getSurname1() != null) {
                    Assertions.assertTrue(authors.get(i).getSurname1().equals(authorSurname[i]));
                }
            }
            
    }
    
    /**
     * Aquest test comproba si funciona correctament les operacions insert, select by id i delete d'autors a JDBC
     */
    @Test
    public void InsertSelectDeleteAuthor() 
    {
        Author a = new Author("Sergio", "Noya", "Cambeiro", "Bio", "España", 1997);
        int id = -1;
        try {
            id = DBAuthor.insertNewAuthor(a);
            Utils.commit();
            System.out.println(id);
            String authorname = DBAuthor.getAuthor(id).getAuthorname();
            Assertions.assertTrue(authorname.equals(a.getAuthorname()));
        } catch(ServerException ex) {
            System.out.println(ex.getMessage());
            Assertions.fail();
        }
        try {
            DBAuthor.deleteAuthor(id);
            Utils.commit();
            a = DBAuthor.getAuthor(id);
            Assertions.assertTrue(a == null);
        } catch(ServerException ex) {
            System.out.println(ex.getMessage());
        } finally {
            Utils.closeConnection();
        }
    }
    
    
}
