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
            ObjectOutputStream objectInput = null;
            try {
                objectInput = new ObjectOutputStream(clientSocket.getOutputStream());
                objectInput.writeObject(allUsers); // envio la llista per socket
                objectInput.flush();
                System.out.println("Llista enviada");
            } catch (IOException ex) {
                throw new ServerException(ex);
            } finally {
                try {
                    objectInput.close();
                } catch (IOException ex) {}
            }
        }
    public static void getUserById(Socket clientSocket) {
        System.out.println("Executant getUserById");
        ObjectInputStream objectInput = null;
        ObjectOutputStream objectOutput = null;
        try {
            System.out.println("inicialitzant objectinput");
            objectInput = new ObjectInputStream(clientSocket.getInputStream());
            System.out.println("objectInput inicialitzat");
            int userId = 0; // inicialitzo la id que es buscarà
            userId = objectInput.readInt(); // el client enviarà pel socket un int, l'id d'usuari
            System.out.println("objectInput llegit");
            User user = DBUser.getUserById(userId);
            objectOutput = new ObjectOutputStream(clientSocket.getOutputStream());
            System.out.println("Trobat usuari");
            objectOutput.writeObject(user); // si no ha trobat l'usuari, user té valor null
            objectOutput.flush();
            System.out.println("Usuari enviat");
        } catch (IOException ex) {
            throw new ServerException(ex);
        } finally {
            try {
                objectInput.close();
                objectOutput.close();
            } catch (IOException ex) {
            }
        }
    }
    
    public static void addNewUser(Socket clientSocket)
        {
            System.out.println("Inserint new user");
            ObjectInputStream objectInput = null;
            ObjectOutputStream objectOutput = null;
            
            try {
                objectInput = new ObjectInputStream(clientSocket.getInputStream());
                User newUser = (User) objectInput.readObject();
                System.out.println("Rebut " + newUser.getFullName());
                System.out.println("Inserint...");
                int userid = DBUser.insertUser(newUser);
                System.out.println("Inserit!");
                objectOutput = new ObjectOutputStream(clientSocket.getOutputStream());
                objectOutput.writeInt(userid);
                objectOutput.flush();
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
                System.out.println("User ID rebuda");
                User updatedUser = (User) objectInput.readObject(); 
                System.out.println("User " + updatedUser.getFullName() + " rebut");
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
                int userId = -1; // inicialitzo la id que es buscarà
                userId = objectInput.readInt(); // el client enviarà pel socket un int, l'id d'usuari
                System.out.println("Rebut ID " + userId);
                DBUser.deleteUserById(userId);
                System.out.println("Eliminat!");
            } catch (IOException ex) {
                throw new ServerException(ex);
            } finally {
                try {
                    objectInput.close();
                } catch (IOException ex) {}
            }
        }
}
