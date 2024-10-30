package app.servidor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class JDBCUtils {
    /*
    ATRIBUTS DE LA CONNEXIÓ
    - url és la direcció sencera de la database, p.ex: "jdbc:postgresql://localhost/ProductDB"
    - user el nom d'usuari de postgres
    - password és la contrasenya de l'usuari
    */
    
    private static String url = "jdbc:postgresql://localhost/libraryapp";
    private static String user = "library_app_admin";
    private static String password = "1234";
    
    // Connexió JDBC amb postgres
    private static Connection conJDBC = null;
    
    
    /*
    setProperties(String url, String user, String password) inicialitza
    els atributs de la connexió
    */
    
    /*
    openConnection() s'executa quan es vol obrir la connexió JDBC
    la funció primer comproba si si ja hi havia una connexió, i si no n'hi ha
    la crea usant l'url, user i password de la classe Utils
    */
    public static void openConnection() 
    {
        if (conJDBC == null) {
            try {
                conJDBC = DriverManager.getConnection(url, user, password);
                conJDBC.setAutoCommit(false);
            } catch (SQLException ex) {
                throw new ServerException("Error en obrir la connexió JDBC. " + ex.getMessage());
            }
        }
    }
    public static void openConnection(String url, String user, String password) 
    {
        if (conJDBC == null) {
            try {
                conJDBC = DriverManager.getConnection(url, user, password);
                conJDBC.setAutoCommit(false);
            } catch (SQLException ex) {
                throw new ServerException("Error en obrir la connexió JDBC. " + ex.getMessage());
            }
        }
    }
    
    
    
    
    /*
    closeConnection s'executa quan es vol tancar la connexió JDBC
    la funció comproba primer que la connexió no estigui ja tancada
    si no està tancada, primer intenta fer rollback dels canvis no desats
    finalment, intenta tancar la connexió
    */
    public static void closeConnection() {
        if (conJDBC != null) {
            try {
                rollback();
            } catch (Exception ex) {}
            finally {
                try {
                    conJDBC.close();
                } catch (SQLException ex) {
                    throw new ServerException("Error en tancar la connexió JDBC. "+ ex.getMessage());
                }
            }
        }
    }

    /*
    rollback() s'executa quan es vol descartar els canvis a la db
    */
    public static void rollback() {
        try {
            conJDBC.rollback();
        } catch (SQLException ex) {
            throw new ServerException("Error en fer rollback: " + ex.getMessage());
        }
    }
    
    
    /*
    rollback() s'executa quan es vol desar els canvis a la db
    */
    public static void commit()
    {
        try{
            conJDBC.commit();
        } catch (SQLException ex) {
            throw new ServerException("Error en fer commit: " + ex.getMessage());
        }
    }
    
    /*
    Genera un PreparedStatent a partir d'un string, per poder fer consultes segures SQL
    */
    public static PreparedStatement prepareStatement(String command)
    {
        try {
            PreparedStatement ps = null;
            openConnection();
            ps = conJDBC.prepareStatement(command);
            return ps;
        } catch (ServerException ex) {
            throw ex;
        } catch (SQLException ex) {
            throw new ServerException(ex.getMessage());
        }
    }
    
    /*
    
    */
    
    public static ResultSet getSelect(String statement)
    {
        ResultSet rs = null;
        try {
            openConnection();
            PreparedStatement query = conJDBC.prepareStatement(statement);
            rs = query.executeQuery();
        } catch (Exception ex) {
            throw new ServerException(ex.getMessage());
        }
        return rs;
    }
}
