/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package app.client.demo.media;

import app.crypto.CryptoUtils;
import app.crypto.Stream;
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
        String pswd = CryptoUtils.getGenericPassword();
        String command = "GET_MEDIA_BY_ID";
        System.out.println("Aquesta demo demostrarà la petició GET_MEDIA_BY_ID.");
        System.out.println("\nIniciant socket...");
        System.out.println("Port: " + port);
        System.out.println("IP: " + ip.getHostAddress());
        Socket soc = new Socket(ip, port);
        Stream stream = new Stream(soc);
        System.out.println("Socket Iniciat!");
        System.out.println("Pren enter per llançar la petició");
        scanner.nextLine();
        CryptoUtils.sendString(stream, command, pswd);
        System.out.println("Petició enviada");
        
        
        int id = 1; // ID a cercar
        
        System.out.println("Es demanarà el media amb ID " + id);
        
        
        CryptoUtils.sendInt(stream, id, pswd);
        System.out.println("ID enviada");
        Media media = null;
        
        media = (Media)CryptoUtils.readObject(stream, pswd);
        if (media == null) System.out.println("No s'ha trobat cap media amb ID " + id);
        else {
            System.out.print("ID: " + media.getWorkId() + " - Titol: " + media.getTitle() + " - Autors: ");
                for (Author a : media.getAuthors()) {
                    System.out.print(a.getFullName() + " ");
                }
                System.out.print("\n");
        }
        System.out.println("Tancant socket...");
        soc.close();
        System.out.println("Socket tancat!");
        System.out.println("Pren enter per sortir.");
        scanner.nextLine();
    }
    
}
