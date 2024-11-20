/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.servidor.handler;

import app.model.User;
import app.servidor.entity.DBUser;
import app.servidor.app.ServerException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

/**
 *
 * @author Sergio
 */
public class UserHandler {
    
    public static void getAllUsers(Socket clientSocket) {
            List<User> allUsers = DBUser.getAllUsers();
            ObjectOutputStream sendObjectToClient = null;
            try {
                sendObjectToClient = new ObjectOutputStream(clientSocket.getOutputStream());
                sendObjectToClient.writeObject(allUsers); // envio la llista per socket
                System.out.println("Llista enviada");
            } catch (IOException ex) {
                throw new ServerException(ex);
            } finally {
                try {
                    sendObjectToClient.close();
                } catch (IOException ex) {}
            }
        }
    public static void getUserById(Socket clientSocket) {
        ObjectInputStream objectInput = null;
        ObjectOutputStream sendObjectToClient = null;
        try {
            objectInput = new ObjectInputStream(clientSocket.getInputStream());
            int userId = 0; // inicialitzo la id que es buscarà
            userId = objectInput.readInt(); // el client enviarà pel socket un int, l'id d'usuari
            User user = DBUser.getUserById(userId);
            sendObjectToClient = new ObjectOutputStream(clientSocket.getOutputStream());
            System.out.println("Trobat usuari");
            sendObjectToClient.writeObject(user); // si no ha trobat l'usuari, user té valor null
            System.out.println("Usuari enviat");
        } catch (IOException ex) {
            throw new ServerException(ex);
        } finally {
            try {
                objectInput.close();
                sendObjectToClient.close();
            } catch (IOException ex) {
            }
        }
    }
    
    public static void addNewUser(Socket clientSocket)
        {
            ObjectInputStream objectInput = null;
            ObjectOutputStream objectOutput = null;
            
            try {
                objectInput = new ObjectInputStream(clientSocket.getInputStream());
                User newUser = (User) objectInput.readObject();
                int userid = DBUser.insertUser(newUser);
                objectOutput = new ObjectOutputStream(clientSocket.getOutputStream());
                objectOutput.writeInt(userid);
            } catch (IOException | ClassNotFoundException ex) {
                throw new ServerException(ex);
            } finally {
                try {
                    objectInput.close();
                } catch (IOException ex) {}
            }
        }
    public static void modifyUser(Socket clientSocket) {
            ObjectInputStream objectInput = null;
            try {
                objectInput = new ObjectInputStream(clientSocket.getInputStream());
                int userId = objectInput.readInt();
                User updatedUser = (User) objectInput.readObject(); 
                DBUser.modifyUser(userId, updatedUser);
                System.out.println("modificació correcta");
            } catch (IOException | ClassNotFoundException ex) {
                throw new ServerException(ex);
            } finally {
                try {
                    objectInput.close();
                } catch (IOException ex) {}
            }
        }
    
    public static void deleteUserById(Socket clientSocket)
        {
            ObjectInputStream objectInput = null;
            try {
                objectInput = new ObjectInputStream(clientSocket.getInputStream());
                int userId = 0; // inicialitzo la id que es buscarà
                userId = objectInput.readInt(); // el client enviarà pel socket un int, l'id d'usuari
                DBUser.deleteUserById(userId);
            } catch (IOException ex) {
                throw new ServerException(ex);
            } finally {
                try {
                    objectInput.close();
                } catch (IOException ex) {}
            }
        }
}
