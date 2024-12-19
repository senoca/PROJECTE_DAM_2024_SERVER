package app.servidor.app;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Classe amb eines per administrar la connexió servidor-client i la gestió de la base de dades.
 * Aquesta classe proporciona mètodes per obrir, tancar i gestionar transaccions JDBC, així com per
 * gestionar connexions i enviament de dades a través de sockets.
 * @author Sergio
 */
public class Utils {
    /*
    ATRIBUTS DE LA CONNEXIÓ
    - url és la direcció sencera de la base de dades, p.ex: "jdbc:postgresql://localhost/ProductDB"
    - user és el nom d'usuari de PostgreSQL
    - password és la contrasenya de l'usuari
    */
    
    private static String url = "jdbc:postgresql://localhost/libraryapp";
    private static String user = "library_app_admin";
    private static String password = "1234";
    
    // Connexió JDBC amb postgres
    private static Connection conJDBC = null;

    /**
     * Obrir la connexió JDBC amb la configuració per defecte.
     * Aquesta funció comprova si ja existeix una connexió oberta, i si no n'hi ha, crea una nova.
     * Si la connexió es crea correctament, es desactiva el commit automàtic.
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

    /**
     * Obrir la connexió JDBC amb configuració personalitzada.
     * Aquesta funció comprova si ja existeix una connexió oberta, i si no n'hi ha, crea una nova
     * amb l'URL, usuari i contrasenya proporcionats.
     * @param url Direcció per connectar-se a la base de dades PostgreSQL.
     * @param user Usuari per a la connexió a PostgreSQL.
     * @param password Contrasenya de l'usuari per a la connexió a PostgreSQL.
     */
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
    La funció comprova primer que la connexió no estigui ja tancada.
    Si no està tancada, primer intenta fer rollback dels canvis no desats
    finalment, intenta tancar la connexió.
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
    
    /**
     * Executa una consulta SELECT i retorna un ResultSet amb el resultat.
     * Aquesta funció utilitza la connexió JDBC per executar una consulta SQL de tipus SELECT.
     * @param statement String amb la consulta SQL.
     * @return ResultSet amb el resultat de la consulta.
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

    /**
     * Tanca un ObjectInputStream.
     * @param in ObjectInputStream que es vol tancar.
     */
    public static void closeObjectInputStream(ObjectInputStream in) {
        if (in != null) {
            try {
                in.close();
            } catch (IOException ex) {
                throw new ServerException(ex);
            }
        }
    }

    /**
     * tanca un objectOutputStream
     * @param out objectOutputStream a tancar
     */
    public static void closeObjectOutputStream(ObjectOutputStream out) {
        if (out != null) {
            try {
                out.close();
            } catch (IOException ex) {
                throw new ServerException(ex);
            }
        }
    }

    /**
     * Envia el string txt pel socket soc usant printwriter
     * @param txt missatge a enviar
     * @param soc socket output
     */
    public static void sendStringToClient(String txt, Socket soc) {
        try {
            PrintWriter write = new PrintWriter(soc.getOutputStream(), true);
            write.println(txt);
            write.close();
        } catch (Exception ex) {
            throw new ServerException(ex);
        }
    }
}
