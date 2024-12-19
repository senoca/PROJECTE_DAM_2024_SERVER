/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package app.client.demo.log;

import app.crypto.CryptoUtils;
import app.crypto.Stream;
import app.model.User;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author Sergio
 */
public class DemoLog {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        login();
    }
    
    public static void login() throws IOException {
        int port = 12345;
        
        InetAddress ip = InetAddress.getLocalHost();
        Scanner scanner = new Scanner(System.in);
        String pswd = CryptoUtils.getGenericPassword();
        String txt = "LOGIN";
        System.out.println("Aquesta demo demostrarà la petició LOGIN.");
        System.out.println("\nIniciant socket...");
        System.out.println("Port: " + port);
        System.out.println("IP: " + ip.getHostAddress());
        Socket soc = new Socket(ip, port);
        System.out.println("Socket Iniciat");
        Stream stream = new Stream(soc);
        System.out.println("Stream iniciat");
        
        
        System.out.println("Pren enter per llançar la petició");
        scanner.nextLine();
        CryptoUtils.sendString(stream, txt, pswd);
        System.out.println("Petició enviada");
        
        CryptoUtils.sendString(stream, "senoca", pswd);
        CryptoUtils.sendString(stream, CryptoUtils.encryptPassword("4321"), pswd);
        System.out.println("1 - " + CryptoUtils.readString(stream, pswd));
        System.out.println("2 - " + CryptoUtils.readString(stream, pswd));
        System.out.println("3 - " + CryptoUtils.readString(stream, pswd));
        System.out.println("4 - " + CryptoUtils.readString(stream, pswd));
        
        
        
        
        
        
        
        System.out.println("Tancant socket...");
        soc.close();
        System.out.println("Socket tancat!");
        System.out.println("Pren enter per sortir.");
        scanner.nextLine();
    }
    
}
