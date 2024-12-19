package app.servidor.app;

import app.servidor.app.ClientThread;
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

/**
 * Aquesta classe és el punt d'entrada del servidor.
 * S'encarrega d'esperar les connexions dels clients, i quan un client es connecta,
 * es crea un nou ClientThread per gestionar la seva comunicació.
 * @author Sergio
 */
public class ServerApp {

    // Mapa en memoria para almacenar las sesiones activas, con el identificador de sesión como clave.
    private static HashMap<String, User> activeSessions = new HashMap<>();

    /**
     * Funció principal del servidor.
     * El servidor escolta les connexions entrants i genera un nou thread per cada client.
     * @param args Paràmetres de línia de comandes (no utilitzats en aquest cas).
     */
    public static void main(String[] args) {
        int port = 12345;  // Puerto en el que escuchará el servidor
        
        int qtThread = 1;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor de sockets escoltant en el port " + port);

            while (true) {
                // Acepta la conexión del cliente
                Socket clientSocket = serverSocket.accept();
                System.out.println("--- " + qtThread +" ---");
                System.out.println("Client connectat");
                    
                // Manejar cada cliente en un nuevo hilo
                new ClientThread(serverSocket, clientSocket, activeSessions).start();
                qtThread++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
}

