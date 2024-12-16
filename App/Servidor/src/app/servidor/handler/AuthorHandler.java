/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.servidor.handler;

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
     * @param clientSocket
     */
    public static void getAllAuthors(Socket clientSocket) {
        ObjectOutputStream objectOutput = null;
        System.out.println("Iniciant GetAllAuthors");
        try {
            System.out.println("generant llista");
            ArrayList<Author> authors = DBAuthor.getAllAuthors();
            System.out.println("llista generada");
            objectOutput = new ObjectOutputStream(clientSocket.getOutputStream());
            System.out.println("canal output generat");
            objectOutput.writeObject((Object) authors);
            System.out.println("llista enviada");
            objectOutput.flush();
            System.out.println("flush");
        } catch (IOException ex) {
            throw new ServerException(ex);
        } finally {
            Utils.closeObjectOutputStream(objectOutput);
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
     * rep per socket un client autor, i l'afegeix al servidor
     * @param clientSocket
     */
    public static void addNewAuthor(Socket clientSocket) {
        ObjectInputStream objectInput = null;
        ObjectOutputStream objectOutput = null;
        System.out.println("Executant addNewAuthor");
        try {
            System.out.println("Rebent nou autor");
            objectInput = new ObjectInputStream(clientSocket.getInputStream());
            Author author = (Author) objectInput.readObject();
            System.out.println("Autor: " + author.getFullName());
            int authorId = DBAuthor.insertNewAuthor(author);
            System.out.println("Inserit! ID generada: " + authorId);
            objectOutput = new ObjectOutputStream(clientSocket.getOutputStream());
            //Utils.sendStringToClient("AUTHOR_CREATED", clientSocket);
            objectOutput.writeInt(authorId);
            objectOutput.flush();
            //Utils.sendStringToClient(authorId + "", clientSocket);
        } catch (IOException | ClassNotFoundException ex) {
            Utils.sendStringToClient("ADD_AUTHOR_FAIL: " + ex.getMessage(), clientSocket);
            Utils.closeObjectInputStream(objectInput);
            Utils.closeObjectOutputStream(objectOutput);
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
