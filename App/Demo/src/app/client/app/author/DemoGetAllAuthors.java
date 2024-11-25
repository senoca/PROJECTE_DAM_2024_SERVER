/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package app.client.app.author;

import app.model.Author;
import java.io.IOException;
import java.io.ObjectInputStream;
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
public class DemoGetAllAuthors {

    /**
     * FUNCIONAMENT CORRECTE
     * 
     */
    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
        int port = 12345;
        InetAddress ip = InetAddress.getLocalHost();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Aquesta demo demostrarà la petició GET_ALL_AUTHORS.");
        System.out.println("\nIniciant socket...");
        System.out.println("Port: " + port);
        System.out.println("IP: " + ip.getHostAddress());
        Socket soc = new Socket(ip, port);
        System.out.println("Socket Iniciat!");
        System.out.println("Pren enter per llançar la petició");
        PrintWriter writeToServer = new PrintWriter(soc.getOutputStream(), true);
        writeToServer.println("GET_ALL_AUTHORS");
        System.out.println("Petició enviada");
        ObjectInputStream objectInput = new ObjectInputStream(soc.getInputStream());
        ArrayList<Author> authors = null;
        authors = (ArrayList<Author>) objectInput.readObject();
        System.out.println("\nAutors trobats: " + authors.size());
        for (Author a : authors)
        {
            System.out.println(a.getAuthorid() + " - " + a.getFullName());
        }
        
        
        
        System.out.println("Tancant socket...");
        soc.close();
        System.out.println("Socket tancat!");
        System.out.println("Pren enter per sortir.");
        scanner.nextLine();
    }
    
}
