package database_console;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
 
public class JdbcInsertFile {
 
    public static void main(String[] args) {
        String url = "jdbc://mysql:mac-addresses.c44pfwy0p40v.us-east-2.rds.amazonaws.com:3306/mac";
        String user = "admin";
        String password = "rs111111"; 
        try {
            Connection conn = DriverManager.getConnection(url, user, password);
 
            String sql = "INSERT INTO data (mac_address) values (?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            
            String fileLocation = args[0];
            File file = new File(fileLocation);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null)
            {
            	statement.setString(1, line);
            	int row = statement.executeUpdate();
                if (row > 0) {
                    System.out.println("A mac address was inserted.");
            }  
 
            
            }
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}