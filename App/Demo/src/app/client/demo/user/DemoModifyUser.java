/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package app.client.demo.user;

import app.model.User;
import app.model.UserType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 *
 * @author Sergio
 */
public class DemoModifyUser {

    /**
     * @param args the command line arguments
     */
    private int port = 12345;
    private InetAddress ip = null;
    
    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
        int port = 12345;
        InetAddress ip = InetAddress.getLocalHost();
        
        Scanner scanner = new Scanner(System.in);
        System.out.println("Prem enter per iniciar socket");
        scanner.nextLine();
        System.out.println("Port: " + port);
        System.out.println("IP: " + ip.toString());
        Socket soc = new Socket(ip, port);
        
        PrintWriter writeToServer = new PrintWriter(soc.getOutputStream(), true);
        BufferedReader readFromClient = new BufferedReader(new InputStreamReader(soc.getInputStream()));
        
        System.out.println("\nModifica un nou usuari prenent ENTER");
        scanner.nextLine();
        User u = new User("bookenjoyer", "1234", "Pepe", "Gutierrez", UserType.ADMIN);
        
        writeToServer.println("MODIFY_USER");
        
        ObjectOutputStream objectOutput = new ObjectOutputStream(soc.getOutputStream());
        
        
        int userId = 11;
        
        
        
        objectOutput.writeInt(userId);
        objectOutput.writeObject(u);
        objectOutput.flush();
        System.out.println("User " + u.getFullName() + " enviat");
        
        
        
        
        
        System.out.println("Pren enter per tancar la demo");
        scanner.nextLine();
        writeToServer.close();
        readFromClient.close();
        objectOutput.close();
        soc.close();
    }
    
}
