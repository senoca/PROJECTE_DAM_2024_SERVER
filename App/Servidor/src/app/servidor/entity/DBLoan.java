/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.servidor.entity;

import app.model.Loan;
import app.model.Media;
import app.model.User;
import app.servidor.app.ServerException;
import app.servidor.app.Utils;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Aquesta classe conté funcions per gestionar les operacions relacionades amb la taula "LOANS" de la base de dades.
 * Permet inserir nous préstecs, eliminar préstecs i obtenir tots els préstecs.
 * @author Sergio
 */
public class DBLoan {
    private static PreparedStatement insertLoanStatement = null;
    private static PreparedStatement deleteLoanStatement = null;
    
    /**
     * Insereix un nou préstec a la base de dades.
     * 
     * Aquesta funció afegeix un registre a la taula "LOANS" amb la informació del préstec proporcionat.
     * @param loan l'objecte Loan que conté les dades a inserir a la base de dades
     * @throws ServerException si es produeix un error a l'inserir el préstec
     */
    public static void insertNewLoan(Loan loan) {
        try {
            if (loan == null)
            {
                throw new ServerException("Error en insertNewLoan: loan nul");
            }
            System.out.println("Inserint data");
            String statement = "insert into LOANS (workid, userid, returned, date_start_loan, date_end_loan) values (?,?,?,?,?)";
            if (insertLoanStatement == null) {
                insertLoanStatement = Utils.prepareStatement(statement);
            }
            insertLoanStatement.setInt(1, loan.getLoanedMedia().getWorkId());
            insertLoanStatement.setInt(2, loan.getUser().getId());
            insertLoanStatement.setBoolean(3, loan.isIsReturned());
            insertLoanStatement.setDate(4, loan.getDateStartLoan());
            insertLoanStatement.setDate(5, loan.getDateEndLoan());
            insertLoanStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new ServerException(ex);
        }
    }
    
    /**
     * Elimina un préstec de la base de dades.
     * 
     * Aquesta funció esborra un préstec de la taula "LOANS" utilitzant l'identificador del treball (workId).
     * @param workId l'identificador del treball prestat que es vol eliminar
     * @throws ServerException si es produeix un error a l'eliminar el préstec
     */
    public static void deleteLoan(int workId) {
        try {
            System.out.println("Eliminant data");
            String statement = "delete from LOANS where workid = ?";
            if (deleteLoanStatement == null) {
                deleteLoanStatement = Utils.prepareStatement(statement);
            }
            deleteLoanStatement.setInt(1, workId);
            deleteLoanStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new ServerException(ex);
        }
    }
    
    /**
     * Recupera tots els préstecs de la base de dades.
     * 
     * Aquesta funció executa una consulta SQL per obtenir tots els préstecs existents a la taula "LOANS" i els retorna
     * en una llista d'objectes Loan.
     * @return una llista d'objectes Loan amb la informació de tots els préstecs
     * @throws ServerException si es produeix un error en recuperar els préstecs
     */
    public static ArrayList<Loan> getAllLoans() {
        ArrayList<Loan> loans = new ArrayList<Loan>();
        try {
            String statement = "select workid, userid, returned, date_start_loan, date_end_loan from loans";
            ResultSet rs = Utils.getSelect(statement);
            while(rs.next()) {
                int workId = rs.getInt("workid");
                Media m = DBMedia.getMediaById(workId);
                int userId = rs.getInt("userid");
                User u = DBUser.getUserById(userId);
                Date dateStartLoan = rs.getDate("date_start_loan");
                Date dateEndLoan = rs.getDate("date_end_loan");                
                Loan l = new Loan(m, u, dateStartLoan, dateEndLoan);
                loans.add(l);
            }
        } catch (SQLException ex) {
            throw new ServerException(ex);
        }
        return loans;
    }
}
