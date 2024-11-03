package app.servidor;

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
                } else if ("GET_ALL_USERS".equals(command)) {
                    try {
                        getAllUsers();
                    } catch (ServerException ex){
                        System.out.println(ex.getMessage());
                    }
                } else if ("GET_USER_BY_ID".equals(command)) {
                    try {
                        getUserById();
                    } catch (ServerException ex){
                        System.out.println(ex.getMessage());
                    }
                } else if ("ADD_USER".equals(command)) {
                    try {
                        addNewUser();
                    } catch (ServerException ex){
                        System.out.println(ex.getMessage());
                    }
                } else if ("DELETE_USER".equals(command)) {
                    try {
                        deleteUserById();
                    } catch (ServerException ex){
                        System.out.println(ex.getMessage());
                    }
                } else if ("MODIFY_USER".equals(command)) {
                    try {
                        modifyUser();
                    } catch (ServerException ex){
                        System.out.println(ex.getMessage());
                    }
                } 
                else {
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

        private void getAllUsers() {
            List<User> allUsers = DBUser.getAllUsers();
            ObjectOutputStream sendObjectToClient = null;
            try {
                sendObjectToClient = new ObjectOutputStream(clientSocket.getOutputStream());
                sendObjectToClient.writeObject(allUsers); // envio la llista per socket
                System.out.println("Llista enviada");
            } catch (IOException ex) {
                throw new ServerException(ex);
            } finally {
                try {
                    sendObjectToClient.close();
                } catch (IOException ex) {}
            }
        }
        
        private void getUserById() {
            ObjectInputStream readObjectFromClient = null;
            ObjectOutputStream sendObjectToClient = null;
            try {
                readObjectFromClient = new ObjectInputStream(clientSocket.getInputStream());
                int userId = 0; // inicialitzo la id que es buscarà
                userId = readObjectFromClient.readInt(); // el client enviarà pel socket un int, l'id d'usuari
                User user = DBUser.getUserById(userId);
                sendObjectToClient = new ObjectOutputStream(clientSocket.getOutputStream());
                System.out.println("Trobat usuari");
                sendObjectToClient.writeObject(user); // si no ha trobat l'usuari, user té valor null 
                System.out.println("Usuari enviat");
            } catch (IOException ex) {
                throw new ServerException(ex);
            } finally {
                try {
                    readObjectFromClient.close();
                    sendObjectToClient.close();
                } catch (IOException ex) {}
            }
        }
        
        private void deleteUserById()
        {
            ObjectInputStream readObjectFromClient = null;
            try {
                readObjectFromClient = new ObjectInputStream(clientSocket.getInputStream());
                int userId = 0; // inicialitzo la id que es buscarà
                userId = readObjectFromClient.readInt(); // el client enviarà pel socket un int, l'id d'usuari
                DBUser.deleteUserById(userId);
            } catch (IOException ex) {
                throw new ServerException(ex);
            } finally {
                try {
                    readObjectFromClient.close();
                } catch (IOException ex) {}
            }
        }
        
        private void addNewUser()
        {
            ObjectInputStream readObjectFromClient = null;
            
            try {
                readObjectFromClient = new ObjectInputStream(clientSocket.getInputStream());
                User newUser = (User) readObjectFromClient.readObject(); 
                /*
                SOBRE USERID DE NEWUSER!!!
                la userId introduida pel client és completament ignorada pel servidor, 
                doncs la base de dades en generarà una que sigui única. Per tant, la id real
                del newUser serà diferent de la que indica el client
                */
                DBUser.insertUser(newUser);
                System.out.println("inserció correcta");
            } catch (IOException | ClassNotFoundException ex) {
                throw new ServerException(ex);
            } finally {
                try {
                    readObjectFromClient.close();
                } catch (IOException ex) {}
            }
        }
        
        private void modifyUser() {
            ObjectInputStream readObjectFromClient = null;
            try {
                readObjectFromClient = new ObjectInputStream(clientSocket.getInputStream());
                int userId = readObjectFromClient.readInt();
                User updatedUser = (User) readObjectFromClient.readObject(); 
                DBUser.modifyUser(userId, updatedUser);
                System.out.println("modificació correcta");
            } catch (IOException | ClassNotFoundException ex) {
                throw new ServerException(ex);
            } finally {
                try {
                    readObjectFromClient.close();
                } catch (IOException ex) {}
            }
        }
    }
}

