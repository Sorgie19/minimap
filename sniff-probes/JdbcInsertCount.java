import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcInsertCount
{

	public static void main(String[] args)
	{
		String url = "jdbc:mysql://mac-addresses.c44pfwy0p40v.us-east-2.rds.amazonaws.com:3306/numbers";
		String user = "admin";
		String password = "rs111111";
		try
		{
			Connection conn = DriverManager.getConnection(url, user, password);

			String sql = "INSERT INTO devices (count) values (?)";
			PreparedStatement statement = conn.prepareStatement(sql);

			String fileLocation = args[0];
			File file = new File(fileLocation);
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			int count = 0;
			while ((line = br.readLine()) != null)
			{
				count++;


			}
			statement.setInt(1, count);
			int row = statement.executeUpdate();
			if (row > 0)
			{
				System.out.println(count + " was inserted.");
			}
			conn.close();
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}
}
