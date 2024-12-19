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
 *
 * @author Sergio
 */
public class LoanHandler {
    public static void addNewLoan(Stream stream, String pswd) {
        try {
            Loan loan = (Loan) CryptoUtils.readObject(stream, pswd);
            DBLoan.insertNewLoan(loan);
        } catch (Exception ex) {
            throw new ServerException(ex);
        }
    }
    
    public static void deleteLoan(Stream stream, String pswd) {
        try {
            int id = CryptoUtils.readInt(stream, pswd);
            DBLoan.deleteLoan(id);
        } catch (Exception ex) {
            throw new ServerException(ex);
        }
    }
    
    public static void getAllLoans(Stream stream, String pswd) {
        try {
            ArrayList<Loan> loans = DBLoan.getAllLoans();
            CryptoUtils.sendObject(stream, loans, pswd);
        } catch (Exception ex) {
            throw new ServerException(ex);
        }
    }
}
