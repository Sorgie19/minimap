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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class JdbcInsertFile {
 
    public static void main(String[] args) {
        String url = "jdbc:mysql://mac-addresses.c44pfwy0p40v.us-east-2.rds.amazonaws.com:3306/mac";
        String user = "admin";
        String password = "rs111111";
        try {
            Connection conn = DriverManager.getConnection(url, user, password);
	    Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
        ResultSet.CONCUR_UPDATABLE);
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
                    System.out.println("MAC address: " + line + "  was inserted.");
            }
            }
	   ResultSet rs = st.executeQuery("SELECT * FROM data");

           rs = st.executeQuery("SELECT COUNT(*) FROM data");
           // get the number of rows from the result set
           rs.next();
           int rowCount = rs.getInt(1);
           System.out.println(rowCount);

            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
