/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package app.client.demo.loan;

import app.crypto.CryptoUtils;
import app.crypto.Stream;
import app.model.Author;
import app.model.Loan;
import app.model.Media;
import app.model.MediaType;
import app.model.User;
import app.model.UserType;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Sergio
 */
public class DemoGetAllLoans {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        int port = 12345;
        InetAddress ip = InetAddress.getLocalHost();
        Scanner scanner = new Scanner(System.in);
        String cmd = "GET_ALL_LOANS";
        String pswd = CryptoUtils.getGenericPassword();
        System.out.println("Aquesta demo demostrarà la petició GET_ALL_LOANS.");
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
        
        ArrayList<Loan> loans = (ArrayList<Loan>) CryptoUtils.readObject(stream, pswd);
        if (loans.isEmpty()) System.out.println("No hi ha cap préstec...");
        else {
            for(Loan l : loans) {
                System.out.println(l.getUser().getFullName() + " té en préstec " + l.getLoanedMedia().getTitle() + " des de " + l.getDateStartLoan().toString() +" fins a " + l.getDateEndLoan().toString());
            }
        }
        
        
        
        System.out.println("Tancant socket...");
        stream.close();
        soc.close();
        System.out.println("Socket tancat!");
        System.out.println("Pren enter per sortir.");
        scanner.nextLine();
    }
    
}
