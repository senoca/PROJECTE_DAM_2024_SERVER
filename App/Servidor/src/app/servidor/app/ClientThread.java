/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.servidor.app;

import app.model.User;
import app.servidor.handler.AuthorHandler;
import app.servidor.handler.LogHandler;
import app.servidor.handler.MediaHandler;
import app.servidor.handler.UserHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * La classe Client extén threads, i serveix per rebre les peticions d'un client
 * @author Sergio
 */
public class ClientThread extends Thread {
    
        private Socket clientSocket;
        private ServerSocket serverSocket;
        private HashMap<String, User> activeSessions;

    /**
     * Constructor de classe Client
     * @param socket socket per connectar amb el client
     * @param activeSessions hashmap dels usuaris connectats al servidor
     */
    public ClientThread(ServerSocket serverSocket, Socket socket, HashMap<String, User> activeSessions) {
            this.serverSocket = serverSocket;
            this.clientSocket = socket;
            this.activeSessions = activeSessions;
        }
        
    /**
     * Bucle principal on es reben i responen les peticions dels clients
     */
    @Override
        public void run() {
            try {
                System.out.println("Iniciat thread client");
                // Establecer timeout en el socket (30 segundos)
                clientSocket.setSoTimeout(10000);

                // He canviant les variables in i out a readFromClient i writeToClient respectivament, per més claretat de lectura
                BufferedReader readFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter writeToClient = new PrintWriter(clientSocket.getOutputStream(), true);
                System.out.println("Canals inicialitzats");
                // Leer el comando del cliente
                String command = readFromClient.readLine();
                System.out.println("Executant petició: " + command);
                /*
                PETICIONS LOGIN
                */
                if ("LOGOUT".equals(command)) {
                    try {
                        LogHandler.logout(activeSessions, readFromClient, writeToClient);
                    } catch (ServerException ex){
                        System.out.println(ex.getMessage());
                    }
                    
                } else if ("LOGIN".equals(command)) {
                    try {
                        LogHandler.login(activeSessions, readFromClient, writeToClient);
                    } catch (ServerException ex){
                        System.out.println(ex.getMessage());
                    }
                } else if ("GET_PROFILE".equals(command)) {
                    try {
                        LogHandler.getProfile(activeSessions, readFromClient, writeToClient);
                    } catch (ServerException ex){
                        System.out.println(ex.getMessage());
                    }
                } 
                /*
                    PETICIONS CRUD USERS
                */    
                
                
                else if ("GET_ALL_USERS".equals(command)) {
                    try {
                        UserHandler.getAllUsers(clientSocket);
                    } catch (ServerException ex){
                        System.out.println(ex.getMessage());
                    }
                } else if ("GET_USER_BY_ID".equals(command)) {
                    try {
                        UserHandler.getUserById(clientSocket);
                    } catch (ServerException ex){
                        System.out.println(ex.getMessage());
                    }
                } else if ("ADD_USER".equals(command)) {
                    try {
                        UserHandler.addNewUser(clientSocket);
                        Utils.commit();
                    } catch (ServerException ex){
                        Utils.rollback();
                        System.out.println(ex.getMessage());
                    }
                } else if ("DELETE_USER".equals(command)) {
                    try {
                        UserHandler.deleteUserById(clientSocket);
                        Utils.commit();
                    } catch (ServerException ex){
                        Utils.rollback();
                        System.out.println(ex.getMessage());
                    }
                } else if ("MODIFY_USER".equals(command)) {
                    try {
                        UserHandler.modifyUser(clientSocket);
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
                        AuthorHandler.getAllAuthors(clientSocket);
                    } catch (ServerException ex){
                        System.out.println(ex.getMessage());
                    }
                } else if ("GET_AUTHOR_BY_ID".equals(command)) {
                    try {
                        AuthorHandler.getAuthorById(clientSocket);
                    } catch (ServerException ex){
                        System.out.println(ex.getMessage());
                    }
                } else if ("ADD_AUTHOR".equals(command)) {
                    try {
                        AuthorHandler.addNewAuthor(clientSocket);
                        Utils.commit();
                    } catch (ServerException ex) {
                        System.out.println(ex.getMessage());
                        try {
                            Utils.rollback();
                        } catch (Exception exep) {
                            System.out.println(exep.getMessage());
                        }
                    }
                } else if ("MODIFY_AUTHOR".equals(command)) {
                    try {
                        AuthorHandler.modifyAuthor(clientSocket);
                        Utils.commit();
                    } catch (ServerException ex) {
                        System.out.println(ex.getMessage());
                        try {
                            Utils.rollback();
                        } catch (Exception exep) {
                            System.out.println(exep.getMessage());
                        }
                    }
                } else if ("DELETE_AUTHOR".equals(command)) {
                    try {
                        AuthorHandler.deleteAuthor(clientSocket);
                        Utils.commit();
                    } catch (ServerException ex) {
                        System.out.println(ex.getMessage());
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
                        MediaHandler.getAllMedia(clientSocket);
                    } catch (ServerException ex){
                        System.out.println(ex.getMessage());
                    }
                } else if ("GET_MEDIA_BY_ID".equals(command)) {
                    try {
                        MediaHandler.getMediaById(clientSocket);
                    } catch (ServerException ex){
                        System.out.println(ex.getMessage());
                    }
                } else if ("ADD_MEDIA".equals(command)) {
                    try {
                        MediaHandler.addNewMedia(clientSocket);
                        Utils.commit();
                    } catch (ServerException ex) {
                        System.out.println(ex.getMessage());
                        try {
                            Utils.rollback();
                        } catch (Exception exep) {
                            System.out.println(exep.getMessage());
                        }
                    }
                } else if ("MODIFY_MEDIA".equals(command)) {
                    try {
                        MediaHandler.modifyMedia(clientSocket);
                        Utils.commit();
                    } catch (ServerException ex) {
                        System.out.println(ex.getMessage());
                        try {
                            Utils.rollback();
                        } catch (Exception exep) {
                            System.out.println(exep.getMessage());
                        }
                    }
                } else if ("DELETE_MEDIA".equals(command)) {
                    try {
                        MediaHandler.deleteMedia(clientSocket);
                        Utils.commit();
                    } catch (ServerException ex) {
                        System.out.println(ex.getMessage());
                        try {
                            Utils.rollback();
                        } catch (Exception exep) {
                            System.out.println(exep.getMessage());
                        }
                    }
                }
                else {
                    // Comando no reconocido
                    writeToClient.println("ERROR: Ordre no reconeguda");
                    System.err.println("Error: Ordre no reconeguda: " + command);
                }

            } catch (IOException e) {
                if (e.getMessage().contains("Connection reset")) {
                    System.err.println("Error: La conexió ha sigut reiniciada pel client.");
                } else {
                    e.printStackTrace();  // Manejo de otros errores de IO
                }
            } finally {
                try {
                    clientSocket.close();  // Cerrar la conexión con el cliente
                } catch (IOException e) {
                    System.err.println("Error al tancar la conexió amb el client: " + e.getMessage());
                }
            }
        }

        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
    }

