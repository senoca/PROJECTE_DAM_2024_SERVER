/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.servidor.handler;

import app.servidor.app.Client;
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
 *
 * @author Sergio
 */
public class AuthorHandler {

    public static void getAllAuthors(Socket clientSocket) {
        ObjectOutputStream objectOutput = null;
        try {
            ArrayList<Author> authors = DBAuthor.getAllAuthors();
            objectOutput = new ObjectOutputStream(clientSocket.getOutputStream());
            objectOutput.writeObject((Object) authors);
        } catch (IOException ex) {
            throw new ServerException(ex);
        } finally {
            Utils.closeObjectOutputStream(objectOutput);
        }
    }
    
    public static void getAuthorById(Socket clientSocket) {
            ObjectInputStream objectInput = null;
            ObjectOutputStream objectOutput = null;
            try {
                objectInput = new ObjectInputStream(clientSocket.getInputStream());
                int authorId = objectInput.readInt();
                Author author = DBAuthor.getAuthor(authorId);
                objectOutput = new ObjectOutputStream(clientSocket.getOutputStream());
                objectOutput.writeObject((Object)author);
            } catch (IOException ex) {
                throw new ServerException(ex);
            } finally {
                Utils.closeObjectInputStream(objectInput);
                Utils.closeObjectOutputStream(objectOutput);
            }
            
        }
    
    public static void addNewAuthor(Socket clientSocket) {
        ObjectInputStream objectInput = null;
        ObjectOutputStream objectOutput = null;
        try {
            objectInput = new ObjectInputStream(clientSocket.getInputStream());
            Author author = (Author) objectInput.readObject();
            int authorId = DBAuthor.insertNewAuthor(author);
            objectOutput = new ObjectOutputStream(clientSocket.getOutputStream());
            Utils.sendStringToClient("AUTHOR_CREATED", clientSocket);
            //objectOutput.writeInt(authorId);
            Utils.sendStringToClient(authorId + "", clientSocket);
        } catch (IOException | ClassNotFoundException ex) {
            Utils.sendStringToClient("ADD_AUTHOR_FAIL: " + ex.getMessage(), clientSocket);
            Utils.closeObjectInputStream(objectInput);
            Utils.closeObjectOutputStream(objectOutput);
        }
    }
    

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
