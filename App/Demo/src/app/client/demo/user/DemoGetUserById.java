/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package app.client.demo.user;

import app.model.User;
import java.io.IOException;
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
public class DemoGetUserById {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, UnknownHostException, ClassNotFoundException {
        int port = 12345;
        InetAddress ip = InetAddress.getLocalHost();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Aquesta demo demostrarà la petició GET_USER_BY_ID.");
        System.out.println("\nIniciant socket...");
        System.out.println("Port: " + port);
        System.out.println("IP: " + ip.getHostAddress());
        Socket soc = new Socket(ip, port);
        System.out.println("Socket Iniciat!");
        System.out.println("Pren enter per llançar la petició");
        scanner.nextLine();
        PrintWriter writeToServer = new PrintWriter(soc.getOutputStream(), true);
        writeToServer.println("GET_USER_BY_ID");
        System.out.println("Petició enviada");
        
        int userId = 1;
        
        System.out.println("L'usuari a cercar té ID : " + userId);
        
        ObjectOutputStream objOut = new ObjectOutputStream(soc.getOutputStream());
        objOut.writeInt(userId);
        objOut.flush();
        System.out.println("ID enviada");
        
        ObjectInputStream objIn = new ObjectInputStream(soc.getInputStream());
        User user = null;
        user = (User) objIn.readObject();
        System.out.println("L'usuari és " + user.getFullName());
        
        
        System.out.println("Tancant socket...");
        soc.close();
        System.out.println("Socket tancat!");
        System.out.println("Pren enter per sortir.");
        scanner.nextLine();
    }
    
}
