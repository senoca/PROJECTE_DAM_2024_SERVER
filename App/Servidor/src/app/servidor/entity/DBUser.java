package app.servidor.entity;

import app.servidor.app.ServerException;
import app.servidor.app.Utils;
import app.model.User;
import app.model.UserType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DBUser guarda les funcions per administrar les consultes, altes, baixes o modificacions referents a la taula Users.
 * Aquesta classe interactua amb la base de dades per gestionar les operacions relacionades amb els usuaris.
 * @author Sergio
 */
public class DBUser {
    
    private static PreparedStatement insertUserStatement = null;
    private static PreparedStatement deleteUserStatement = null;
    private static PreparedStatement modifyUserStatement = null;
    private static PreparedStatement selectUserByIdStatement = null;
    

    /**
     * Comprova si existeix un usuari amb el nom d'usuari i la contrasenya proporcionats.
     * @param alias nom d'usuari de l'usuari.
     * @param pswd contrasenya de l'usuari.
     * @return true si l'usuari existeix, false si no.
     */

    public static boolean UserExists(String alias, String pswd) {
        User u = getUserFromCredentials(alias, pswd);
        return u != null;
    }

    /**
     * Cerca un usuari a la base de dades amb el nom d'usuari i la contrasenya proporcionats.
     * @param username nom d'usuari.
     * @param pswd contrasenya de l'usuari.
     * @return un objecte User amb les dades de l'usuari si es troba, null si no es troba.
     */

    public static User getUserFromCredentials(String username, String pswd) {
        String query = "SELECT userid, username, userpswd, realname, surname1, surname2, usertype "
                     + "FROM users "
                     + "WHERE username = ? AND userpswd = ?";  // Cambiado de "alias" a "username"
        User user = null;

        try {
            PreparedStatement statement = Utils.prepareStatement(query);
            statement.setString(1, username);   // Sustituye el primer ? por el username
            statement.setString(2, pswd);    // Sustituye el segundo ? por la contraseña

            ResultSet rs = statement.executeQuery();
            
            if (rs.next()) {
                user = buildUserObject(rs);  // Construye el objeto User si se encuentra un resultado
                System.out.println("Usuario encontrado: " + user.getUsername() + " con tipo de usuario: " + user.getType());  // Debug: confirmar si encuentra el usuario y su tipo
            } else {
                System.out.println("Usuario no encontrado con username: " + username + " y contraseña: " + pswd);  // Debug: no encuentra usuario
            }
        } catch (SQLException ex) {
            throw new ServerException(ex.getMessage());
        }
        return user;
    }

    /**
     * Retorna un usuari cercant la seva ID a la base de dades.
     * @param idToSearch ID de l'usuari a cercar.
     * @return un objecte User amb les dades de l'usuari si es troba, null si no es troba.
     */
    public static User getUserById(int idToSearch) {
        String query = "SELECT userid, username, userpswd, realname, surname1, surname2, usertype "
                     + "FROM users "
                     + "WHERE userid = ? ";  // Cambiado de "alias" a "username"
        User user = null;

        try {
            selectUserByIdStatement = Utils.prepareStatement(query);
            selectUserByIdStatement.setInt(1, idToSearch);   // Sustituye el primer ? por el username

            ResultSet rs = selectUserByIdStatement.executeQuery();
            
            if (rs.next()) {
                user = buildUserObject(rs);  // Construye el objeto User si se encuentra un resultado
                System.out.println("Usuario encontrado: " + user.getUsername() + " con tipo de usuario: " + user.getType());  // Debug: confirmar si encuentra el usuario y su tipo
            } else {
                System.out.println("Usuario no encontrado con id: " + idToSearch);  // Debug: no encuentra usuario
            }
        } catch (SQLException ex) {
            throw new ServerException(ex.getMessage());
        }
        return user;
    }
    
    /**
     * Retorna tots els usuaris registrats a la base de dades.
     * @return una llista amb tots els usuaris.
     */
    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<User>();
        String statement = "select userid, username, userpswd, realname, surname1, surname2, usertype\n" +
        "from USERS";
         try {
            ResultSet rs = null;
            rs = Utils.getSelect(statement);
            while (rs.next())
            {
                User user = buildUserObject(rs);
                users.add(user);
            }
        } catch (SQLException ex) {
            throw new ServerException(ex.getMessage());
        } catch (ServerException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServerException(ex.getMessage());
        }
        return users;
    }
    
    /**
     * Crea un objecte User a partir d'un ResultSet de la consulta SQL.
     * @param rs el ResultSet amb les dades de l'usuari.
     * @return un objecte User amb les dades de l'usuari.
     */
    private static User buildUserObject(ResultSet rs) {
        User u;
        try {
            u = new User(
                    rs.getInt("userid"),
                    rs.getString("username"),
                    rs.getString("userpswd"),
                    rs.getString("realname"),
                    rs.getString("surname1"),
                    rs.getString("surname2"),
                    rs.getString("usertype")
            );
        } catch (SQLException ex) {
            throw new ServerException(ex.getMessage());
        }
        return u;
    }
    
    

    /**
     * Obté el tipus d'usuari (USER o ADMIN) a partir del nom d'usuari.
     * @param username nom d'usuari.
     * @return el tipus d'usuari com a cadena (USER o ADMIN).
     */
    public static String getUserType(String username) {
        String query = "SELECT usertype FROM users WHERE username = ?";
        String userType = null;
        
        try {
            PreparedStatement statement = Utils.prepareStatement(query);
            statement.setString(1, username);   // Sustituye el primer ? por el username
            ResultSet rs = statement.executeQuery();
            
            if (rs.next()) {
                userType = rs.getString("usertype");
                System.out.println("Tipo de usuario: " + userType);
            } else {
                System.out.println("Tipo de usuario no encontrado para username: " + username);
            }
        } catch (SQLException ex) {
            throw new ServerException(ex.getMessage());
        }
        return userType;
    }

    /**
     * Insereix un nou usuari a la base de dades.
     * @param user l'objecte User que es vol inserir.
     * @return l'ID de l'usuari inserit.
     */
    public static int insertUser(User user) 
    {
        int userid = -1;
        try {
            if (user == null)
            {
                throw new ServerException("Error en insertUser: usuari nul");
            }
            String statement = "insert into USERS ("
                    + "username,"
                    + "userpswd,"
                    + "realname,"
                    + "surname1,"
                    + "surname2,"
                    + "usertype"
                    + ")"
                    + "values (?,?,?,?,?,?) "
                    + "returning userid";
            if (insertUserStatement == null) {
                System.out.println("generant statement");
                insertUserStatement = Utils.prepareStatement(statement);
            }
            insertUserStatement = Utils.prepareStatement(statement);
            System.out.println("preparant statement");
            insertUserStatement.setString(1, user.getUsername());
            insertUserStatement.setString(2, user.getPassword());
            insertUserStatement.setString(3, user.getRealname());
            insertUserStatement.setString(4, user.getSurname1());
            insertUserStatement.setString(5, user.getSurname2());
            insertUserStatement.setString(6, user.getTypeAsString());
            insertUserStatement.execute();
            ResultSet rs = insertUserStatement.getResultSet();
            if (rs.next()) {
                userid = rs.getInt("userid");
            }
            if (userid == -1) throw new ServerException ("Error: insertUser no ha generat userid correcte");
            
        } catch (SQLException ex) {
            throw new ServerException (ex.getMessage());
        }
        return userid;
    }
    
    /**
     * Esborra un usuari de la base de dades segons la seva ID.
     * @param userid ID de l'usuari a esborrar.
     */
    public static void deleteUserById(int userid) {
        try {
            String statement = "delete from users where userid = ?";
            if (deleteUserStatement == null) {
                deleteUserStatement = Utils.prepareStatement(statement);
            }
            deleteUserStatement.setInt(1, userid);
            deleteUserStatement.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            throw new ServerException(ex);
        }
    }
    
    /**
     * Modifica les dades d'un usuari identificat per la seva ID amb les noves dades.
     * @param userId ID de l'usuari a modificar.
     * @param newUser objecte User amb les noves dades.
     */
    public static void modifyUser(int userId, User newUser) {
        try {
            String statement = "update USERS set "
                    + "username = ?,"
                    + "userpswd = ?,"
                    + "realname = ?,"
                    + "surname1 = ?,"
                    + "surname2 = ?,"
                    + "usertype = ? "
                    + "where userid = ? ";
            if (modifyUserStatement == null) {
                modifyUserStatement = Utils.prepareStatement(statement);
            }
            modifyUserStatement.setString(1, newUser.getUsername());
            modifyUserStatement.setString(2, newUser.getPassword());
            modifyUserStatement.setString(3, newUser.getRealname());
            modifyUserStatement.setString(4, newUser.getSurname1());
            modifyUserStatement.setString(5, newUser.getSurname2());
            modifyUserStatement.setString(6, newUser.getTypeAsString());
            modifyUserStatement.setInt(7, userId);
            modifyUserStatement.executeUpdate();
        } catch (SQLException ex) {
            throw new ServerException(ex);
        }
        
    }
}
