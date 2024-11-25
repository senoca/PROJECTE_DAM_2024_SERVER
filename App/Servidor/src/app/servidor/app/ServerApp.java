package app.servidor.app;

import app.servidor.app.Client;
import app.model.User;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerApp {

    // Mapa en memoria para almacenar las sesiones activas, con el identificador de sesión como clave.
    private static HashMap<String, User> activeSessions = new HashMap<>();

    /**
     *
     * @param args 
     * Main del servidor
     */
    public static void main(String[] args) {
        int port = 12345;  // Puerto en el que escuchará el servidor
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor de sockets escoltant en el port " + port);

            while (true) {
                // Acepta la conexión del cliente
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connectat");

                // Manejar cada cliente en un nuevo hilo
                new Client(serverSocket, clientSocket, activeSessions).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
}

