/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.servidor.handler;

import app.crypto.CryptoUtils;
import app.crypto.Stream;
import app.model.Loan;
import app.servidor.app.ServerException;
import app.servidor.entity.DBLoan;
import java.util.ArrayList;

/**
 * LoanHandler gestiona les peticions relacionades amb els préstecs.
 * Aquesta classe inclou mètodes per afegir, esborrar i obtenir llistes de préstecs.
 * La comunicació amb el client es realitza de forma segura mitjançant encriptació.
 * @author Sergio
 */
public class LoanHandler {
    /**
     * Rep un objecte Loan del client i l'afegeix a la base de dades.
     * @param stream el flux de comunicació entre el servidor i el client.
     * @param pswd la contrasenya utilitzada per encriptar la comunicació.
     */
    public static void addNewLoan(Stream stream, String pswd) {
        try {
            Loan loan = (Loan) CryptoUtils.readObject(stream, pswd);
            DBLoan.insertNewLoan(loan);
        } catch (Exception ex) {
            throw new ServerException(ex);
        }
    }
    
    /**
     * Rep un ID de préstec del client i el suprimeix de la base de dades.
     * @param stream el flux de comunicació entre el servidor i el client.
     * @param pswd la contrasenya utilitzada per encriptar la comunicació.
     */
    public static void deleteLoan(Stream stream, String pswd) {
        try {
            int id = CryptoUtils.readInt(stream, pswd);
            DBLoan.deleteLoan(id);
        } catch (Exception ex) {
            throw new ServerException(ex);
        }
    }
    
    /**
     * Obté tots els préstecs de la base de dades i els envia al client de manera segura.
     * @param stream el flux de comunicació entre el servidor i el client.
     * @param pswd la contrasenya utilitzada per encriptar la comunicació.
     */
    public static void getAllLoans(Stream stream, String pswd) {
        try {
            ArrayList<Loan> loans = DBLoan.getAllLoans();
            CryptoUtils.sendObject(stream, loans, pswd);
        } catch (Exception ex) {
            throw new ServerException(ex);
        }
    }
}
