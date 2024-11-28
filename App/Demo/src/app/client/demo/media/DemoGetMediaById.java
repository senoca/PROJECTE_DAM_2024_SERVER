/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package app.client.demo.media;

import app.model.Author;
import app.model.Media;
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
public class DemoGetMediaById {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
        int port = 12345;
        InetAddress ip = InetAddress.getLocalHost();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Aquesta demo demostrarà la petició GET_MEDIA_BY_ID.");
        System.out.println("\nIniciant socket...");
        System.out.println("Port: " + port);
        System.out.println("IP: " + ip.getHostAddress());
        Socket soc = new Socket(ip, port);
        System.out.println("Socket Iniciat!");
        System.out.println("Pren enter per llançar la petició");
        scanner.nextLine();
        PrintWriter writeToServer = new PrintWriter(soc.getOutputStream(), true);
        writeToServer.println("GET_MEDIA_BY_ID");
        System.out.println("Petició enviada");
        
        
        int id = 1; // ID a cercar
        
        System.out.println("Es demanarà el media amb ID " + id);
        
        ObjectOutputStream objectOutput = new ObjectOutputStream(soc.getOutputStream());
        
        objectOutput.writeInt(id);
        objectOutput.flush();
        System.out.println("ID enviada");
        Media media = null;
        ObjectInputStream objectInput = new ObjectInputStream(soc.getInputStream());
        media = (Media)objectInput.readObject();
        if (media == null) System.out.println("No s'ha trobat cap media amb ID " + id);
        else {
            System.out.println("S'ha trobat: " + media.getTitle() + " amb els autors:");
            for (Author a : media.getAuthors())
            {
                System.out.println(a.getFullName());
            }
        }
        System.out.println("Tancant socket...");
        soc.close();
        System.out.println("Socket tancat!");
        System.out.println("Pren enter per sortir.");
        scanner.nextLine();
    }
    
}
