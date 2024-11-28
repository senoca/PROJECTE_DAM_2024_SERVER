/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package app.client.demo.author;

import app.model.Author;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Sergio
 */
public class DemoGetAuthorById {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        int port = 12345;
        InetAddress ip = InetAddress.getLocalHost();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Aquesta demo demostrarà la petició GET_AUTHOR_BY_ID.");
        System.out.println("\nIniciant socket...");
        System.out.println("Port: " + port);
        System.out.println("IP: " + ip.getHostAddress());
        Socket soc = new Socket(ip, port);
        System.out.println("Socket Iniciat!");
        System.out.println("Pren enter per llançar la petició");
        scanner.nextLine();
        PrintWriter writeToServer = new PrintWriter(soc.getOutputStream(), true);
        writeToServer.println("GET_AUTHOR_BY_ID");
        System.out.println("Petició enviada");
        
        
        int id = 33; // ID a cercar
        
        System.out.println("Es demanarà l'autor amb ID " + id);
        
        ObjectOutputStream objectOutput = new ObjectOutputStream(soc.getOutputStream());
        
        objectOutput.writeInt(id);
        objectOutput.flush();
        System.out.println("ID enviada");
        Author author = null;
        ObjectInputStream objectInput = new ObjectInputStream(soc.getInputStream());
        author = (Author)objectInput.readObject();
        if (author == null) System.out.println("No s'ha trobat cap autor amb ID " + id);
        else System.out.println("S'ha trobat: " + author.getFullName());
        System.out.println("Tancant socket...");
        soc.close();
        System.out.println("Socket tancat!");
        System.out.println("Pren enter per sortir.");
        scanner.nextLine();
    }
    
}
