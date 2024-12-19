/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.servidor.handler;

import app.crypto.CryptoUtils;
import app.crypto.Stream;
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
    public static void getAllAuthors(Stream stream, String pswd) {
        
        System.out.println("Iniciant GetAllAuthors");
        try {
            System.out.println("generant llista");
            ArrayList<Author> authors = DBAuthor.getAllAuthors();
            System.out.println("llista generada");
            
            CryptoUtils.sendObject(stream, (Object)authors, pswd);
            
            System.out.println("llista enviada");
            
            
        } catch (Exception ex) {
            throw new ServerException(ex);
        } 
    }
    
       
    /**
     * VERSIÓ CRYPTO 
     * rep per socket un id, i retorna per socket l'autor corresponent
     * @param soc
     * @param pswd contrasenya per encriptació
     */
    public static void getAuthorById(Stream stream, String pswd) {
        System.out.println("Executant authorHandler.getAuthorById");
        try {
            int authorId = CryptoUtils.readInt(stream, pswd);
            System.out.println("Rebuda ID: " + authorId);
            Author author = DBAuthor.getAuthor(authorId);
            if (author == null) System.out.println("No s'ha trobat cap autor amb ID " + authorId);
            else System.out.println("S'ha trobat: " + author.getFullName());
            System.out.println("Enviant author");
            
            CryptoUtils.sendObject(stream, author, pswd);
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
    public static void addNewAuthor(Stream stream, String pswd) {
        System.out.println("Executant addNewAuthor");
        try {
            System.out.println("Rebent nou autor");
            Object obj = CryptoUtils.readObject(stream, pswd);
            System.out.println("Rebut: " + obj.toString());
            Author author = (Author) obj;
            System.out.println("Autor: " + author.getFullName());
            int authorId = DBAuthor.insertNewAuthor(author);
            System.out.println("Inserit! ID generada: " + authorId);
            CryptoUtils.sendInt(stream, authorId, pswd);
            
        } catch (Exception ex) {
            throw new ServerException(ex);
        }
    }
    
    /**
     * rep per socket un client amb dades modificades, i el sobreescriu
     * @param clientSocket
     */
    public static void modifyAuthor(Stream stream, String pswd) {
        
            try {
                Author updatedAuthor = (Author) CryptoUtils.readObject(stream, pswd);
                DBAuthor.updateNewAuthor(updatedAuthor.getAuthorid(), updatedAuthor);
            } catch (Exception ex) {
                throw new ServerException(ex);
            }
        }

    /**
     * rep per socket un id, i esborra de la bd el autor corresponent
     * @param clientSocket
     */
    public static void deleteAuthor(Stream stream, String pswd) {
        
        try {
            int authorId = CryptoUtils.readInt(stream, pswd);
            DBAuthor.deleteAuthor(authorId);
        } catch (Exception ex) {
            throw new ServerException(ex);
        }
    }
    
}
