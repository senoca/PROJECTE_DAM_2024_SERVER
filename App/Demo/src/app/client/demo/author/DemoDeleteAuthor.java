/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package app.client.demo.author;

import app.crypto.CryptoUtils;
import app.crypto.Stream;
import app.model.Author;
import java.io.IOException;
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
public class DemoDeleteAuthor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, UnknownHostException {
        int port = 12345;
        InetAddress ip = InetAddress.getLocalHost();
        Scanner scanner = new Scanner(System.in);
        String pswd = CryptoUtils.getGenericPassword();
        System.out.println("Aquesta demo demostrarà la petició DELETE_AUTHOR.");
        System.out.println("\nIniciant socket...");
        System.out.println("Port: " + port);
        System.out.println("IP: " + ip.getHostAddress());
        Socket soc = new Socket(ip, port);
        Stream stream = new Stream(soc);
        System.out.println("Socket Iniciat!");
        System.out.println("Pren enter per llançar la petició");
        scanner.nextLine();
        CryptoUtils.sendString(stream, "DELETE_AUTHOR", pswd);
        System.out.println("Petició enviada");
        
        int authorId = 5;
        
        System.out.println("L'autor a crear eliminar té ID : " + authorId);
        
        
        
        CryptoUtils.sendInt(stream, authorId, pswd);
        System.out.println("Autor enviat");
        
        
        System.out.println("Tancant socket...");
        soc.close();
        System.out.println("Socket tancat!");
        System.out.println("Pren enter per sortir.");
        scanner.nextLine();
    }
    
}
