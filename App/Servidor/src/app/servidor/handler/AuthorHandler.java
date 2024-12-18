/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.servidor.handler;

import app.crypto.CryptoUtils;
import app.servidor.app.ClientThread;
import app.model.Author;
import app.model.Media;
import app.model.User;
import app.servidor.entity.DBAuthor;
import app.servidor.app.ServerException;
import app.servidor.app.Utils;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * AuthorHandler resol totes les peticions client-usuari relacionades amb autors
 * @author Sergio
 */
public class AuthorHandler {

    /**
     * envia per socket una llista amb tots els autors de la db
     * @param soc
     */
    public static void getAllAuthors(Socket soc, String pswd) {
        
        System.out.println("Iniciant GetAllAuthors");
        try {
            System.out.println("generant llista");
            ArrayList<Author> authors = DBAuthor.getAllAuthors();
            System.out.println("llista generada");
            
            CryptoUtils.sendObject(soc.getOutputStream(), (Object)authors, pswd);
            
            System.out.println("llista enviada");
            
            
        } catch (IOException ex) {
            throw new ServerException(ex);
        } 
    }
    
    /**
     * rep per socket un id, i retorna per socket l'autor corresponent
     * @param clientSocket
     */
    public static void getAuthorById(Socket clientSocket) {
        System.out.println("Executant authorHandler.getAuthorById");
            ObjectInputStream objectInput = null;
            ObjectOutputStream objectOutput = null;
            try {
                objectInput = new ObjectInputStream(clientSocket.getInputStream());
                int authorId = objectInput.readInt();
                System.out.println("Rebuda ID: " + authorId);
                Author author = DBAuthor.getAuthor(authorId);
                if (author == null) System.out.println("No s'ha trobat cap autor amb ID " + authorId);
                else System.out.println("S'ha trobat: " + author.getFullName());
                System.out.println("Enviant author");
                objectOutput = new ObjectOutputStream(clientSocket.getOutputStream());
                objectOutput.writeObject((Object)author);
                objectOutput.flush();
                System.out.println("Autor enviat");
            } catch (IOException ex) {
                throw new ServerException(ex);
            } finally {
                Utils.closeObjectInputStream(objectInput);
                Utils.closeObjectOutputStream(objectOutput);
            }
            
        }
    
    /**
     * VERSIÓ CRYPTO 
     * rep per socket un id, i retorna per socket l'autor corresponent
     * @param soc
     * @param pswd contrasenya per encriptació
     */
    public static void getAuthorById(Socket soc, String pswd) {
        System.out.println("Executant authorHandler.getAuthorById");
        try {
            int authorId = CryptoUtils.readInt(soc.getInputStream(), pswd);
            System.out.println("Rebuda ID: " + authorId);
            Author author = DBAuthor.getAuthor(authorId);
            if (author == null) System.out.println("No s'ha trobat cap autor amb ID " + authorId);
            else System.out.println("S'ha trobat: " + author.getFullName());
            System.out.println("Enviant author");
            
            CryptoUtils.sendObject(soc.getOutputStream(), author, pswd);
            System.out.println("Autor enviat");
        } catch (Exception ex) {
            throw new ServerException(ex);
        }
    }
    
    /**
     * rep per socket un client autor, i l'afegeix al servidor
     * @param soc
     * @param pswd
     */
    public static void addNewAuthor(Socket soc, String pswd) {
        System.out.println("Executant addNewAuthor");
        try {
            System.out.println("Rebent nou autor");
            Object obj = CryptoUtils.readObject(soc.getInputStream(), pswd);
            System.out.println("Rebut: " + obj.toString());
            Author author = (Author) obj;
            System.out.println("Autor: " + author.getFullName());
            int authorId = DBAuthor.insertNewAuthor(author);
            System.out.println("Inserit! ID generada: " + authorId);
            CryptoUtils.sendInt(soc.getOutputStream(), authorId, pswd);
            
        } catch (Exception ex) {
            throw new ServerException(ex);
        }
    }
    
    /**
     * rep per socket un client amb dades modificades, i el sobreescriu
     * @param clientSocket
     */
    public static void modifyAuthor(Socket clientSocket) {
        ObjectInputStream objectInput = null;
            try {
                objectInput = new ObjectInputStream(clientSocket.getInputStream());
                Author updatedAuthor = (Author) objectInput.readObject();
                DBAuthor.updateNewAuthor(updatedAuthor.getAuthorid(), updatedAuthor);
            } catch (Exception ex) {
                throw new ServerException(ex);
            } finally {
                Utils.closeObjectInputStream(objectInput);
            }
        }

    /**
     * rep per socket un id, i esborra de la bd el autor corresponent
     * @param clientSocket
     */
    public static void deleteAuthor(Socket clientSocket) {
        ObjectInputStream objectInput = null;
        try {
            objectInput = new ObjectInputStream(clientSocket.getInputStream());
            int authorId = objectInput.readInt();
            DBAuthor.deleteAuthor(authorId);
        } catch (Exception ex) {
            throw new ServerException(ex);
        } finally {
            Utils.closeObjectInputStream(objectInput);
        }
    }
    
}
