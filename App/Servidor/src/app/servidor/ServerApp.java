package app.servidor;

import app.model.User;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerApp {

    // Mapa en memoria para almacenar las sesiones activas, con el identificador de sesión como clave.
    private static HashMap<String, User> activeSessions = new HashMap<>();

    public static void main(String[] args) {
        int port = 12345;  // Puerto en el que escuchará el servidor
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Servidor de sockets escoltant en el port " + port);

            while (true) {
                // Acepta la conexión del cliente
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connectat");

                // Manejar cada cliente en un nuevo hilo
                new ClientHandler(clientSocket).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Clase para manejar cada cliente de manera independiente
    static class ClientHandler extends Thread {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                // Establecer timeout en el socket (30 segundos)
                clientSocket.setSoTimeout(30000);

                // He canviant les variables in i out a readFromClient i writeToClient respectivament, per més claretat de lectura
                BufferedReader readFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter writeToClient = new PrintWriter(clientSocket.getOutputStream(), true);

                // Leer el comando del cliente
                String command = readFromClient.readLine();
                
                if ("LOGOUT".equals(command)) {
                    try {
                        logout(activeSessions, readFromClient, writeToClient);
                    } catch (ServerException ex){
                        System.out.println(ex.getMessage());
                    }
                    
                } else if ("LOGIN".equals(command)) {
                    try {
                        login(activeSessions, readFromClient, writeToClient);
                    } catch (ServerException ex){
                        System.out.println(ex.getMessage());
                    }

                } else if ("GET_PROFILE".equals(command)) {
                    try {
                        getProfile(activeSessions, readFromClient, writeToClient);
                    } catch (ServerException ex){
                        System.out.println(ex.getMessage());
                    }
                } else {
                    // Comando no reconocido
                    writeToClient.println("ERROR: Ordre no reconeguda");
                    System.err.println("Error: Orde no reconeguda: " + command);
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

        private void logout(HashMap<String, User> activeSessions, BufferedReader readFromClient, PrintWriter writeToClient) {
            try {
                // Manejar el proceso de logout
                String sessionId = readFromClient.readLine(); // El cliente debe enviar el identificador de sesión
                
                if (activeSessions.containsKey(sessionId)) {
                    activeSessions.remove(sessionId);
                    writeToClient.println("LOGOUT_OK");
                    System.out.println("Logout exitòs per a la sessió: " + sessionId);
                } else {
                    writeToClient.println("LOGOUT_FAIL: Sesió no trobada");
                    System.out.println("Intent de logout fallit. Sessió no trobada: " + sessionId);
                }
            } catch (IOException ex) {
                throw new ServerException(ex);
            }
        }

        private void login(HashMap<String, User> activeSessions, BufferedReader readFromClient, PrintWriter writeToClient) {
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
                        
                        System.out.println("Login exitòs per usuari: " + username + ", sessió: " + sessionId);
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

        private void getProfile(HashMap<String, User> activeSessions, BufferedReader readFromClient, PrintWriter writeToClient) {
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
                    
                    System.out.println("Perfil enviat per la sessió: " + sessionId);
                } else {
                    writeToClient.println("ERROR: Sessió no válida");
                    System.out.println("Error al obtenir perfil. Sessió no válida: " + sessionId);
                }
            } catch (IOException ex) {
                throw new ServerException(ex);
            }
        }
    }
}

