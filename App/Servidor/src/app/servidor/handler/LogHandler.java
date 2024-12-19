/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.servidor.handler;

import app.crypto.CryptoUtils;
import app.crypto.Stream;
import app.model.User;
import app.servidor.app.ServerException;
import app.servidor.entity.DBUser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.UUID;

/**
 * Aquesta classe conté les funcions relacionades amb login i logout
 * @author Sergio
 */
public class LogHandler {

    /**
     *
     * @param activeSessions
     * @param readFromClient
     * @param writeToClient
     */
    public static void logout(HashMap<String, User> activeSessions, Socket soc, String pswd) {
//        try {
//            // Manejar el proceso de logout
//            String sessionId = CryptoUtils.readString(soc.getInputStream(), pswd); // El cliente debe enviar el identificador de sesión
//            if (activeSessions.containsKey(sessionId)) {
//                activeSessions.remove(sessionId);
//                CryptoUtils.sendString(soc.getOutputStream(), "LOGOUT_OK", pswd);
//                System.out.println("Logout exit\u00f2s per a la sessi\u00f3: " + sessionId);
//            } else {
//                CryptoUtils.sendString(soc.getOutputStream(), "LOGOUT_FAIL: Sesi\u00f3 no trobada", pswd);
//                System.out.println("Intent de logout fallit. Sessi\u00f3 no trobada: " + sessionId);
//            }
//        } catch (IOException ex) {
//            throw new ServerException(ex);
//        }
    }

    /**
     *
     * @param activeSessions
     * @param readFromClient
     * @param writeToClient
     */
    public static void login(HashMap<String, User> activeSessions, Stream stream, String pswd) {
            // Proceso de login
        try {
            String username = null;
            String password = null;
            username = CryptoUtils.readString(stream, pswd);
            System.out.println("Username: " + username);
            password = CryptoUtils.readString(stream, pswd);
            System.out.println("Password: " + password);
            if (username == null || password == null) {
                CryptoUtils.sendString(stream, "ERROR: Username o password no rebut.", pswd);
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
                    CryptoUtils.sendString(stream, "LOGIN_OK", pswd);
                    CryptoUtils.sendString(stream, "SESSION_ID:" + sessionId, pswd);
                    CryptoUtils.sendString(stream, "USER_TYPE:" + user.getType(), pswd);
                    CryptoUtils.sendString(stream, "USER_ID:" + user.getId(), pswd);
                    
                    System.out.println("Login exit\u00f2s per usuari: " + username + ", sessi\u00f3: " + sessionId);
                } else {
                    // Devolver error si las credenciales no son válidas
                    //CryptoUtils.sendString(soc.getOutputStream(), "LOGIN_FAIL", pswd);
                    System.out.println("Login fallit per usuari: " + username);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ServerException(ex);
        }
    }

    /**
     *
     * @param activeSessions
     * @param readFromClient
     * @param writeToClient
     */
    public static void getProfile(HashMap<String, User> activeSessions, Socket soc, String pswd) {
//        try {
//            // Comando para obtener el perfil del usuario
//            String sessionId = CryptoUtils.readString(soc.getInputStream(), pswd);
//            if (activeSessions.containsKey(sessionId)) {
//                User user = activeSessions.get(sessionId);
//                // Enviar la información del perfil al cliente
//                CryptoUtils.sendString(soc.getOutputStream(), user.getUsername(), pswd);
//                CryptoUtils.sendString(soc.getOutputStream(), user.getRealname(), pswd);
//                CryptoUtils.sendString(soc.getOutputStream(), user.getSurname1(), pswd);
//                CryptoUtils.sendString(soc.getOutputStream(), user.getSurname2() != null ? user.getSurname2() : "", pswd);
//                CryptoUtils.sendString(soc.getOutputStream(), user.getTypeAsString(), pswd);
//                CryptoUtils.sendString(soc.getOutputStream(), user.getPassword(), pswd);
//                System.out.println("Perfil enviat per la sessi\u00f3: " + sessionId);
//            } else {
//                CryptoUtils.sendString(soc.getOutputStream(), "ERROR: Sessi\u00f3 no v\u00e1lida", pswd);
//                System.out.println("Error al obtenir perfil. Sessi\u00f3 no v\u00e1lida: " + sessionId);
//            }
//        } catch (IOException ex) {
//            throw new ServerException(ex);
//        }
    }
    
}
