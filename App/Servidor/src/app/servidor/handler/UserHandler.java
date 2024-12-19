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
 * Aquesta classe conté les funcions per resoldre les peticions client-servidor relacionades amb usuaris.
 * Inclou operacions per obtenir, afegir, modificar i esborrar usuaris de la base de dades.
 * @author Sergio
 */
public class UserHandler {
    
    /**
     * Envia per socket una llista amb tots els usuaris de la base de dades.
     * @param stream el flux de comunicació entre el servidor i el client.
     * @param pswd la contrasenya utilitzada per encriptar la comunicació.
     */
    public static void getAllUsers(Stream stream, String pswd) {
            List<User> allUsers = DBUser.getAllUsers();
            
            try {
                CryptoUtils.sendObject(stream, (Object)allUsers, pswd);
                System.out.println("Llista enviada");
            } catch (Exception ex) {
                throw new ServerException(ex);
            }
        }

    /**
     * Rep per socket un id d'usuari i envia l'usuari corresponent.
     * @param stream el flux de comunicació entre el servidor i el client.
     * @param pswd la contrasenya utilitzada per encriptar la comunicació.
     */
    public static void getUserById(Stream stream, String pswd) {
        System.out.println("Executant getUserById");
        try {
            System.out.println("inicialitzant objectinput");
            
            int userId = 0; // inicialitzo la id que es buscarà
            userId = CryptoUtils.readInt(stream, pswd);
            System.out.println("objectInput llegit. Id : " + userId);
            User user = DBUser.getUserById(userId);
            System.out.println("Select executada");
            
            System.out.println("Trobat usuari");
            CryptoUtils.sendObject(stream, (Object)user, pswd);
            System.out.println("Usuari enviat");
        } catch (Exception ex) {
            throw new ServerException(ex);
        }
    }
    
    /**
     * Rep un nou usuari per socket, l'inserta a la base de dades i retorna la nova id generada.
     * @param stream el flux de comunicació entre el servidor i el client.
     * @param pswd la contrasenya utilitzada per encriptar la comunicació.
     */
    public static void addNewUser(Stream stream, String pswd)
        {
            System.out.println("Inserint new user");
            
            try {
                User newUser = (User) CryptoUtils.readObject(stream, pswd);
                System.out.println("Rebut " + newUser.getFullName());
                System.out.println("Inserint...");
                int userid = DBUser.insertUser(newUser);
                System.out.println("Inserit!");
                CryptoUtils.sendInt(stream, userid, pswd);
            } catch (Exception ex) {
                throw new ServerException(ex);
            } 
        }

    /**
     * Rep un usuari modificat per socket i actualitza les seves dades a la base de dades.
     * @param stream el flux de comunicació entre el servidor i el client.
     * @param pswd la contrasenya utilitzada per encriptar la comunicació.
     */
    public static void modifyUser(Stream stream, String pswd) {
            try {
                User u = (User) CryptoUtils.readObject(stream, pswd);
                System.out.println("User " + u.getFullName() + " rebut");
                DBUser.modifyUser(u.getId(), u);
                System.out.println("modificació correcta");
            } catch (Exception ex) {
                throw new ServerException(ex);
            } 
        }
    
    /**
     * Rep per socket un id d'usuari i esborra l'usuari corresponent de la base de dades.
     * @param stream el flux de comunicació entre el servidor i el client.
     * @param pswd la contrasenya utilitzada per encriptar la comunicació.
     */
    public static void deleteUserById(Stream stream, String pswd)
        {
            
            try {
                
                int userId = -1; // inicialitzo la id que es buscarà
                userId = CryptoUtils.readInt(stream, pswd); // el client enviarà pel socket un int, l'id d'usuari
                System.out.println("Rebut ID " + userId);
                DBUser.deleteUserById(userId);
                System.out.println("Eliminat!");
            } catch (Exception ex) {
                throw new ServerException(ex);
            }
        }
}
