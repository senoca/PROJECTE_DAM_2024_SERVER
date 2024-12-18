/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package app.client.demo.user;

import app.crypto.CryptoUtils;
import app.model.Author;
import app.model.User;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Sergio
 */
public class DemoGetAllUsers {

    /**
     * @param args the command line arguments
     */
    private int port = 12345;
    private InetAddress ip = null;
    
    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
        int port = 12345;
        InetAddress ip = InetAddress.getLocalHost();
        String pswd = CryptoUtils.getGenericPassword();
        String txt = "GET_ALL_USERS";
        Scanner scanner = new Scanner(System.in);
        System.out.println("Prem enter per iniciar socket");
        scanner.nextLine();
        System.out.println("Port: " + port);
        System.out.println("IP: " + ip.toString());
        Socket soc = new Socket(ip, port);
        
        
        
        System.out.println("\nFes enter per GET_ALL_USERS, i rebre les dades de tots els usuaris");
        scanner.nextLine();
        CryptoUtils.sendString(soc.getOutputStream(), txt, pswd);
        System.out.println("Processant...");
        
        ArrayList<User> users = (ArrayList<User>) CryptoUtils.readObject(soc.getInputStream(), pswd);
        for (User u : users)
        {
            System.out.println("ID " + u.getId() + " - " + u.getFullName() + " - " + u.getTypeAsString());
        }
        
        
        System.out.println("Pren enter per tancar la demo");
        scanner.nextLine();
        soc.close();
    }
    
}
