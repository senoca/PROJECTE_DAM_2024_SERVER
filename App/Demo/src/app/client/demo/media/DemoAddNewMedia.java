/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package app.client.demo.media;

import app.client.demo.author.*;
import app.crypto.CryptoUtils;
import app.crypto.Stream;
import app.model.Author;
import app.model.Media;
import app.model.MediaType;
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
public class DemoAddNewMedia {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        int port = 12345;
        InetAddress ip = InetAddress.getLocalHost();
        Scanner scanner = new Scanner(System.in);
        String cmd = "ADD_MEDIA";
        String pswd = CryptoUtils.getGenericPassword();
        System.out.println("Aquesta demo demostrarà la petició ADD_MEDIA.");
        System.out.println("\nIniciant socket...");
        System.out.println("Port: " + port);
        System.out.println("IP: " + ip.getHostAddress());
        Socket soc = new Socket(ip, port);
        Stream stream = new Stream(soc);
        System.out.println("Socket Iniciat!");
        System.out.println("Pren enter per llançar la petició");
        scanner.nextLine();
        CryptoUtils.sendString(stream, cmd, pswd);
        System.out.println("Petició enviada");
        Media m = new Media("El Hòbbit", 1939, MediaType.BOOK, "Fantasía");
        Author a = new Author(3, "John Ronald", "Tolkien", null, "Escritor de Fantasía", "Reino Unido", 1892);
        m.addAuthor(a);
        System.out.println("El llibre a crear a crear és : " + m.getTitle());
        
        CryptoUtils.sendObject(stream, m, pswd);
        System.out.println("Llibre enviat");
        Integer id = CryptoUtils.readInt(stream, pswd);
        System.out.println("La ID generada es " + id);
        
        
        System.out.println("Tancant socket...");
        soc.close();
        System.out.println("Socket tancat!");
        System.out.println("Pren enter per sortir.");
        scanner.nextLine();
    }
    
}
