/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.servidor.app;

import app.crypto.CryptoUtils;
import app.crypto.Stream;
import app.model.User;
import app.servidor.handler.AuthorHandler;
import app.servidor.handler.LogHandler;
import app.servidor.handler.MediaHandler;
import app.servidor.handler.UserHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * La classe Client extén threads, i serveix per rebre les peticions d'un client
 * @author Sergio
 */
public class ClientThread extends Thread {
    
        private final Socket soc;
        private ServerSocket serverSocket;
        private HashMap<String, User> activeSessions;
        private final Stream stream;

    /**
     * Constructor de classe Client
     * @param socket socket per connectar amb el client
     * @param activeSessions hashmap dels usuaris connectats al servidor
     */
    public ClientThread(ServerSocket serverSocket, Socket socket, HashMap<String, User> activeSessions) {
            this.serverSocket = serverSocket;
            this.soc = socket;
            this.activeSessions = activeSessions;
            this.stream = new Stream(soc);
            System.out.println("Stream iniciat");
        }
        
    /**
     * Bucle principal on es reben i responen les peticions dels clients
     */
    @Override
        public void run() {
            try (soc) {
                System.out.println("Iniciat thread client");
                // Establecer timeout en el socket (30 segundos)
                soc.setSoTimeout(5000);
                
                // Leer el comando del cliente
                String pswd = CryptoUtils.getGenericPassword();
                String command = CryptoUtils.readString(stream, pswd);
                System.out.println("Executant petició: " + command);
                /*
                PETICIONS LOGIN
                */
                if ("LOGOUT".equals(command)) {
                    try {
                        LogHandler.logout(activeSessions, stream, pswd);
                    } catch (ServerException ex){
                        System.out.println(ex.getMessage());
                    }
                    
                } else if ("LOGIN".equals(command)) {
                    try {
                        LogHandler.login(activeSessions, stream, pswd);
                    } catch (ServerException ex){
                        System.out.println(ex.getMessage());
                    }
                } else if ("GET_PROFILE".equals(command)) {
                    try {
                        LogHandler.getProfile(activeSessions, stream, pswd);
                    } catch (ServerException ex){
                        System.out.println(ex.getMessage());
                    }
                } 
                /*
                    PETICIONS CRUD USERS
                */    
                
                
                else if ("GET_ALL_USERS".equals(command)) {
                    try {
                        UserHandler.getAllUsers(stream, pswd);
                    } catch (ServerException ex){
                        System.out.println(ex.getMessage());
                    }
                } else if ("GET_USER_BY_ID".equals(command)) {
                    try {
                        UserHandler.getUserById(stream, pswd);
                    } catch (ServerException ex){
                        System.out.println(ex.getMessage());
                    }
                } else if ("ADD_USER".equals(command)) {
                    try {
                        UserHandler.addNewUser(stream, pswd);
                        Utils.commit();
                    } catch (ServerException ex){
                        Utils.rollback();
                        System.out.println(ex.getMessage());
                    }
                } else if ("DELETE_USER".equals(command)) {
                    try {
                        UserHandler.deleteUserById(stream, pswd);
                        Utils.commit();
                    } catch (ServerException ex){
                        Utils.rollback();
                        System.out.println(ex.getMessage());
                    }
                } else if ("MODIFY_USER".equals(command)) {
                    try {
                        UserHandler.modifyUser(stream, pswd);
                        Utils.commit();
                    } catch (ServerException ex){
                        Utils.rollback();
                        System.out.println(ex.getMessage());
                    }
                }     
                /*
                    PETICIONS CRUD AUTHORS
                */    
                else if ("GET_ALL_AUTHORS".equals(command)){
                    try {
                        AuthorHandler.getAllAuthors(stream, pswd);
                    } catch (ServerException ex){
                        ex.printStackTrace();
                    }
                } else if ("GET_AUTHOR_BY_ID".equals(command)) {
                    try {
                        AuthorHandler.getAuthorById(stream, pswd);
                    } catch (ServerException ex){
                        ex.printStackTrace();
                    }
                } else if ("ADD_AUTHOR".equals(command)) {
                    try {
                        AuthorHandler.addNewAuthor(stream, pswd);
                        Utils.commit();
                    } catch (ServerException ex) {
                        ex.printStackTrace();
                        try {
                            Utils.rollback();
                        } catch (Exception exep) {
                            System.out.println(exep.getMessage());
                        }
                    }
                } else if ("MODIFY_AUTHOR".equals(command)) {
                    try {
                        AuthorHandler.modifyAuthor(stream, pswd);
                        Utils.commit();
                    } catch (ServerException ex) {
                        ex.printStackTrace();
                        try {
                            Utils.rollback();
                        } catch (Exception exep) {
                            System.out.println(exep.getMessage());
                        }
                    }
                } else if ("DELETE_AUTHOR".equals(command)) {
                    try {
                        AuthorHandler.deleteAuthor(stream, pswd);
                        Utils.commit();
                    } catch (ServerException ex) {
                        ex.printStackTrace();
                        try {
                            Utils.rollback();
                        } catch (Exception exep) {
                            System.out.println(exep.getMessage());
                        }
                    }
                }
                /*
                PETICIONS CRUD MEDIA
                */
                else if ("GET_ALL_MEDIA".equals(command)){
                    try {
                        MediaHandler.getAllMedia(stream, pswd);
                    } catch (ServerException ex){
                        ex.printStackTrace();
                    }
                } else if ("GET_MEDIA_BY_ID".equals(command)) {
                    try {
                        MediaHandler.getMediaById(stream, pswd);
                    } catch (ServerException ex){
                        ex.printStackTrace();
                    }
                } else if ("ADD_MEDIA".equals(command)) {
                    try {
                        MediaHandler.addNewMedia(stream, pswd);
                        Utils.commit();
                    } catch (ServerException ex) {
                        ex.printStackTrace();
                        try {
                            Utils.rollback();
                        } catch (Exception exep) {
                            System.out.println(exep.getMessage());
                        }
                    }
                } else if ("MODIFY_MEDIA".equals(command)) {
                    try {
                        MediaHandler.modifyMedia(stream, pswd);
                        Utils.commit();
                    } catch (ServerException ex) {
                        ex.printStackTrace();
                        try {
                            Utils.rollback();
                        } catch (Exception exep) {
                            System.out.println(exep.getMessage());
                        }
                    }
                } else if ("DELETE_MEDIA".equals(command)) {
                    try {
                        MediaHandler.deleteMedia(stream, pswd);
                        Utils.commit();
                    } catch (ServerException ex) {
                        ex.printStackTrace();
                        try {
                            Utils.rollback();
                        } catch (Exception exep) {
                            System.out.println(exep.getMessage());
                        }
                    }
                }
                else {
                    // Comando no reconocido
                    CryptoUtils.sendString(stream, pswd, "ERROR: Ordre no reconeguda");
//                    writeToClient.println("ERROR: Ordre no reconeguda");
                    System.err.println("Error: Ordre no reconeguda: " + command);
                }
                stream.close();
            } catch (Exception e) {
                /*
                if (e.getMessage().contains("Connection reset")) {
                    System.err.println("Error: La conexió ha sigut reiniciada pel client.");
                } else {
                    e.printStackTrace();  // Manejo de otros errores de IO
                }
                */
                throw new ServerException(e);
            } 
            System.out.println("Socket tancat");
            /*
            finally {
                try {
                    
                    soc.close();  // Cerrar la conexión con el cliente
                    System.out.println("Socket tancat");
                } catch (IOException e) {
                    System.err.println("Error al tancar la conexió amb el client: " + e.getMessage());
                }
            }
*/
        }

        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
    }

