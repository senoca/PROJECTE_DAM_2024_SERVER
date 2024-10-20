package app.servidor;

import app.model.User;
import java.util.List;

public class TestConnection {
    public static void main(String[] args)
    {
        String url = "jdbc:postgresql://localhost/libraryapp";
        String user = "library_app_admin";
        String password = "1234";
        
        JDBCUtils.openConnection(url, user, password);
        System.out.println("Connexió oberta");
        List<User> users = DBUser.getAllUsers();
        System.out.println("Usuaris trobats:");
        for (User u : users)
        {
            System.out.println(u.getFullName());
        }
        JDBCUtils.closeConnection();
        System.out.println("Connexió tancada");
    }
}
