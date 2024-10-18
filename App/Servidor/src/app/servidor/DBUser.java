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
    public static User getUserFromCredentials(String alias, String pswd) {
        String statement = ""
                + "select userid, useralias, userpswd, username, surname1, surname2, usertype"
                + "from users"
                + "where alias = '" + alias + "' and pswd = '" + pswd + "'";
        User user = null;
        try {
            ResultSet rs = null;
            rs = JDBCUtils.getSelect(statement);
            while (rs.next())
            {
                user = buildUserObject(rs);
            }
        } catch (SQLException ex) {
            throw new ServidorException(ex.getMessage());
        } catch (ServidorException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ServidorException(ex.getMessage());
        }
        return user;
    }

    
    
    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<User>();
        String statement = "select userid, useralias, userpswd, username, surname1, surname2, usertype\n" +
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
                    rs.getString("useralias"),
                    rs.getString("userpswd"),
                    rs.getString("username"),
                    rs.getString("surname1"),
                    rs.getString("surname2"),
                    rs.getString("usertype")
            );
        } catch (SQLException ex) {
            throw new ServidorException(ex.getMessage());
        }
        return u;
    }
    
}
