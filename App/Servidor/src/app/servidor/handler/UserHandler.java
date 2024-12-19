/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.servidor.handler;

import app.crypto.CryptoUtils;
import app.crypto.Stream;
import app.model.User;
import app.servidor.entity.DBUser;
import app.servidor.app.ServerException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

/**
 * Aquesta classe conté les funcions per resoldre les peticions client-servidor relacionades amb usuaris
 * @author Sergio
 */
public class UserHandler {
    
    /**
     * envia per socket una llista amb tots els usuaris
     * @param clientSocket
     */
    public static void getAllUsers(Socket clientSocket, String pswd) {
            List<User> allUsers = DBUser.getAllUsers();
            
            try {
                CryptoUtils.sendObject(clientSocket.getOutputStream(), (Object)allUsers, pswd);
                System.out.println("Llista enviada");
            } catch (IOException ex) {
                throw new ServerException(ex);
            }
        }

    /**
     * rep per socket un id i envia l'usuari corresponent
     * @param soc
     */
    public static void getUserById(Socket soc, String pswd) {
        System.out.println("Executant getUserById");
//        try {
//            System.out.println("inicialitzant objectinput");
//            
//            int userId = 0; // inicialitzo la id que es buscarà
//            userId = CryptoUtils.readInt(soc.getInputStream(), pswd);
//            System.out.println("objectInput llegit. Id : " + userId);
//            User user = DBUser.getUserById(userId);
//            System.out.println("Select executada");
//            
//            System.out.println("Trobat usuari");
//            CryptoUtils.sendObject(soc.getOutputStream(), (Object)user, pswd);
//            System.out.println("Usuari enviat");
//        } catch (IOException ex) {
//            throw new ServerException(ex);
//        }
    }
    
    /**
     * rep un nou usuari i l'inserta a la base de dades, retorna per socket la nova id
     * @param soc
     */
    public static void addNewUser(Stream stream, String pswd)
        {
            System.out.println("Inserint new user");
            
            try {
//                User newUser = (User) CryptoUtils.readObject(soc.getInputStream(), pswd);
//                System.out.println("Rebut " + newUser.getFullName());
//                System.out.println("Inserint...");
//                int userid = DBUser.insertUser(newUser);
//                System.out.println("Inserit!");
//                CryptoUtils.sendInt(stream, userid, pswd);
            } catch (Exception ex) {
                throw new ServerException(ex);
            } 
        }

    /**
     * rep per socket un usuari modificat i el sobreescriu a la bd
     * @param clientSocket
     */
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
    
    /**
     * rep per socket un id i esborra el usuari corresponent
     * @param clientSocket
     */
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
