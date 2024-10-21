package app.servidor;

import app.model.User;
import app.model.UserType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBUser {
    
    
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
            PreparedStatement statement = JDBCUtils.prepareStatement(query);
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
            throw new ServidorException(ex.getMessage());
        }
        return user;
    }

    
    
    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<User>();
        String statement = "select userid, username, userpswd, realname, surname1, surname2, usertype\n" +
        "from USERS";
         try {
            ResultSet rs = null;
            rs = JDBCUtils.getSelect(statement);
            while (rs.next())
            {
                User user = buildUserObject(rs);
                users.add(user);
            }
        } catch (SQLException ex) {
            throw new ServidorException(ex.getMessage());
        } catch (ServidorException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServidorException(ex.getMessage());
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
            throw new ServidorException(ex.getMessage());
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
            PreparedStatement statement = JDBCUtils.prepareStatement(query);
            statement.setString(1, username);   // Sustituye el primer ? por el username
            ResultSet rs = statement.executeQuery();
            
            if (rs.next()) {
                userType = rs.getString("usertype");
                System.out.println("Tipo de usuario: " + userType);
            } else {
                System.out.println("Tipo de usuario no encontrado para username: " + username);
            }
        } catch (SQLException ex) {
            throw new ServidorException(ex.getMessage());
        }
        return userType;
    }
}
