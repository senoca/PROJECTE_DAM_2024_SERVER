/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package app.client.demo.author;

import app.crypto.CryptoUtils;
import app.crypto.Stream;
import app.model.Author;
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
public class DemoModifyAuthor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, UnknownHostException {
        int port = 12345;
        InetAddress ip = InetAddress.getLocalHost();
        Scanner scanner = new Scanner(System.in);   
        String pswd = CryptoUtils.getGenericPassword();
        String txt = "MODIFY_AUTHOR";
        System.out.println("Aquesta demo demostrarà la petició MODIFY_AUTHOR.");
        System.out.println("\nIniciant socket...");
        System.out.println("Port: " + port);
        System.out.println("IP: " + ip.getHostAddress());
        Socket soc = new Socket(ip, port);
        Stream stream = new Stream(soc);
        System.out.println("Socket Iniciat!");
        System.out.println("Pren enter per llançar la petició");
        scanner.nextLine();
        CryptoUtils.sendString(stream, txt, pswd);
        System.out.println("Petició enviada");
        Author a = new Author(5, "Sergio", "Noya", "Cambeiro", "Un escriptor amb futur!", "Espanya", 1997);
        
        System.out.println("L'autor a editar és : " + a.getFullName());
        CryptoUtils.sendObject(stream, a, pswd);
        System.out.println("Autor enviat");
        
        
        System.out.println("Tancant socket...");
        stream.close();
        soc.close();
        System.out.println("Socket tancat!");
        System.out.println("Pren enter per sortir.");
        scanner.nextLine();
    }
    
}
