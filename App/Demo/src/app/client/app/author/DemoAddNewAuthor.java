/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package app.client.app.author;

import app.model.Author;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author Sergio
 */
public class DemoAddNewAuthor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        int port = 12345;
        InetAddress ip = InetAddress.getLocalHost();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Aquesta demo demostrarà la petició ADD_AUTHOR.");
        System.out.println("\nIniciant socket...");
        System.out.println("Port: " + port);
        System.out.println("IP: " + ip.getHostAddress());
        Socket soc = new Socket(ip, port);
        System.out.println("Socket Iniciat!");
        System.out.println("Pren enter per llançar la petició");
        scanner.nextLine();
        PrintWriter writeToServer = new PrintWriter(soc.getOutputStream(), true);
        writeToServer.println("ADD_AUTHOR");
        System.out.println("Petició enviada");
        Author a = new Author("Sergio", "Noya", "Cambeiro", "Un pobre currant...", "Espanya", 1997);
        
        System.out.println("L'autor a crear és : " + a.getFullName());
        
        ObjectOutputStream objOut = new ObjectOutputStream(soc.getOutputStream());
        objOut.writeObject(a);
        objOut.flush();
        System.out.println("Autor enviat");
        int id = -1;
        
        ObjectInputStream objInt = new ObjectInputStream(soc.getInputStream());
        
        id = objInt.readInt();
        System.out.println("La ID generada es " + id);
        
        
        System.out.println("Tancant socket...");
        soc.close();
        System.out.println("Socket tancat!");
        System.out.println("Pren enter per sortir.");
        scanner.nextLine();
    }
    
}
