/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.servidor.handler;

import app.model.User;
import app.servidor.app.ServerException;
import app.servidor.entity.DBUser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.UUID;

/**
 *
 * @author Sergio
 */
public class LogHandler {

    public static void logout(HashMap<String, User> activeSessions, BufferedReader readFromClient, PrintWriter writeToClient) {
        try {
            // Manejar el proceso de logout
            String sessionId = readFromClient.readLine(); // El cliente debe enviar el identificador de sesión
            if (activeSessions.containsKey(sessionId)) {
                activeSessions.remove(sessionId);
                writeToClient.println("LOGOUT_OK");
                System.out.println("Logout exit\u00f2s per a la sessi\u00f3: " + sessionId);
            } else {
                writeToClient.println("LOGOUT_FAIL: Sesi\u00f3 no trobada");
                System.out.println("Intent de logout fallit. Sessi\u00f3 no trobada: " + sessionId);
            }
        } catch (IOException ex) {
            throw new ServerException(ex);
        }
    }

    public static void login(HashMap<String, User> activeSessions, BufferedReader readFromClient, PrintWriter writeToClient) {
        try {
            // Proceso de login
            String username = readFromClient.readLine();
            String password = readFromClient.readLine();
            if (username == null || password == null) {
                writeToClient.println("ERROR: Username o password no rebut.");
            } else {
                System.out.println("Credencials rebudes: " + username + " / " + password);
                // Verificar las credenciales usando DBUser
                User user = DBUser.getUserFromCredentials(username, password);
                if (user != null) {
                    // Generar un identificador de sesión único
                    String sessionId = UUID.randomUUID().toString();
                    // Almacenar la sesión en memoria
                    activeSessions.put(sessionId, user);
                    // Enviar el identificador de sesión y el tipo de usuario al cliente
                    writeToClient.println("LOGIN_OK");
                    writeToClient.println("SESSION_ID:" + sessionId);
                    writeToClient.println("USER_TYPE:" + user.getType());
                    writeToClient.println("USER_ID:" + user.getId());
                    System.out.println("Login exit\u00f2s per usuari: " + username + ", sessi\u00f3: " + sessionId);
                } else {
                    // Devolver error si las credenciales no son válidas
                    writeToClient.println("LOGIN_FAIL");
                    System.out.println("Login fallit per usuari: " + username);
                }
            }
        } catch (IOException ex) {
            throw new ServerException(ex);
        }
    }

    public static void getProfile(HashMap<String, User> activeSessions, BufferedReader readFromClient, PrintWriter writeToClient) {
        try {
            // Comando para obtener el perfil del usuario
            String sessionId = readFromClient.readLine(); // El cliente debe enviar el identificador de sesión
            if (activeSessions.containsKey(sessionId)) {
                User user = activeSessions.get(sessionId);
                // Enviar la información del perfil al cliente
                writeToClient.println(user.getUsername());
                writeToClient.println(user.getRealname());
                writeToClient.println(user.getSurname1());
                writeToClient.println(user.getSurname2() != null ? user.getSurname2() : "");
                writeToClient.println(user.getType());
                writeToClient.println(user.getPassword());
                System.out.println("Perfil enviat per la sessi\u00f3: " + sessionId);
            } else {
                writeToClient.println("ERROR: Sessi\u00f3 no v\u00e1lida");
                System.out.println("Error al obtenir perfil. Sessi\u00f3 no v\u00e1lida: " + sessionId);
            }
        } catch (IOException ex) {
            throw new ServerException(ex);
        }
    }
    
}
