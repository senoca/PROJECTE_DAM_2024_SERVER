/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package app.servidor;

import app.model.User;
import static app.servidor.DBUser.getUserFromCredentials;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.jupiter.api.Assertions;

/**
 *
 * @author Sergio
 */
public class Testing {
    
    

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // 
    
    @Test
    public void TestGoodConnection() {
        String url = "jdbc:postgresql://localhost/libraryapp";
        String user = "library_app_admin";
        String password = "1234";
        
        JDBCUtils.openConnection(url, user, password);
        JDBCUtils.closeConnection();
    }
    @Test
    public void TestGetUserFromCredentials() {
        String url = "jdbc:postgresql://localhost/libraryapp";
        String user = "library_app_admin";
        String password = "1234";
        
        User u = getUserFromCredentials("senoca", "1234");
        System.out.println(u.getRealname());
        Assertions.assertTrue("Sergio".equals(u.getRealname()));
    }

}
