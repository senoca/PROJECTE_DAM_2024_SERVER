/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.servidor.app;

import app.crypto.CryptoUtils;
import app.crypto.Stream;
import app.model.User;
import app.servidor.handler.AuthorHandler;
import app.servidor.handler.LoanHandler;
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
 * La classe ClientThread extén la classe Thread i serveix per rebre les peticions d'un client
 * i processar-les de manera concurrent.
 * Cada instància d'aquesta classe gestiona la comunicació amb un client connectat al servidor.
 * @author Sergio
 */
public class ClientThread extends Thread {
    
        private final Socket soc;
        private ServerSocket serverSocket;
        private HashMap<String, User> activeSessions;
        private final Stream stream;

    /**
     * Constructor de la classe ClientThread.
     * Inicialitza el socket de comunicació amb el client i el hashmap amb les sessions actives.
     * @param serverSocket El servidor que accepta connexions.
     * @param socket El socket per comunicar-se amb el client.
     * @param activeSessions El mapa de sessions d'usuaris actius.
     */
    public ClientThread(ServerSocket serverSocket, Socket socket, HashMap<String, User> activeSessions) {
            this.serverSocket = serverSocket;
            this.soc = socket;
            this.activeSessions = activeSessions;
            this.stream = new Stream(soc);
            System.out.println("Stream iniciat");
        }
        
    /**
     * Mètode principal que s'executa quan s'inicia el thread.
     * Processa les peticions dels clients en un bucle de recepció i resposta.
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
                
                /*
                PETICIONES DE LOANS
                */
                
                else if ("ADD_LOAN".equals(command)){
                    try {
                        LoanHandler.addNewLoan(stream, pswd);
                        Utils.commit();
                    } catch (ServerException ex){
                        ex.printStackTrace();
                        Utils.rollback();
                    }
                } else if ("DELETE_LOAN".equals(command)){
                    try {
                        LoanHandler.deleteLoan(stream, pswd);
                        Utils.commit();
                    } catch (ServerException ex){
                        ex.printStackTrace();
                        Utils.rollback();
                    }
                } else if ("GET_ALL_LOANS".equals(command)){
                    try {
                        LoanHandler.getAllLoans(stream, pswd);
                    } catch (ServerException ex){
                        ex.printStackTrace();
                        Utils.rollback();
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

