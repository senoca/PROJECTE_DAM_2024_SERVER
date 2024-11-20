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

public class DBUser {
    
    private static PreparedStatement insertUserStatement = null;
    private static PreparedStatement deleteUserStatement = null;
    private static PreparedStatement modifyUserStatement = null;
    private static PreparedStatement selectUserByIdStatement = null;
    
    /*
    Busca si existex un usuari amb alies i pswd introduits, retorna true si existeix i false si no
    */
    public static boolean UserExists(String alias, String pswd) {
        User u = getUserFromCredentials(alias, pswd);
        return u != null;
    }
    
    
    /*
    Busca un únic usuari amb alies i pswd introduits, retorna
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

    public static User getUserById(int idToSearch) {
        String query = "SELECT userid, username, userpswd, realname, surname1, surname2, usertype "
                     + "FROM users "
                     + "WHERE userid = ? ";  // Cambiado de "alias" a "username"
        User user = null;

        try {
            selectUserByIdStatement = Utils.prepareStatement(query);
            selectUserByIdStatement.setString(1, Integer.toString(idToSearch));   // Sustituye el primer ? por el username

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
    
    /*
    Crea un objecte usuari a partir d'un ResultSet
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
    
    
     /*
    Método para obtener el tipo de usuario (USER o ADMIN) por username.
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
     *
     * @param user
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
                insertUserStatement = Utils.prepareStatement(statement);
            }
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
    
    public static void deleteUserById(int userid) {
        try {
            String statement = "delete from users where userid = ?";
            if (deleteUserStatement == null) {
                Utils.prepareStatement(statement);
            }
            deleteUserStatement.setInt(1, userid);
        } catch (SQLException ex) {
            throw new ServerException(ex);
        }
    }
    
    public static void modifyUser(int userId, User newUser) {
        try {
            String statement = "update USERS set "
                    + "username = ?,"
                    + "userpsed = ?,"
                    + "realname = ?,"
                    + "surname1 = ?,"
                    + "surname2 = ?,"
                    + "usertype = ? "
                    + "where userid = ? ";
            if (modifyUserStatement == null) {
                Utils.prepareStatement(statement);
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
