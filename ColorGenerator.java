package MiniMap;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class ColorGenerator
{
	public static void main(String args[]) throws ClassNotFoundException
	{
		final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
		String url = "jdbc:mysql://minimap.ddns.uark.edu:3306/minimap";
		String user = "ryan";
		String password = "minimap";
		Connection conn = null;
		Statement stmt = null;
		ArrayList<Integer> devicesList = new ArrayList<Integer>();
		int lastCapture = 0;
		int count = 0;
		int min = 0;
		int max = 0;
		try
		{
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(url, user, password);
			// STEP 3: Open a connection
			System.out.println("Connecting to a selected database...");
			conn = DriverManager.getConnection(url, user, password);
			System.out.println("Connected database successfully...");
			// STEP 4: Execute a query
			System.out.println("Creating statement...");
			stmt = conn.createStatement();

			String sql = "SELECT entry_id, time_stamp, device_count FROM devices";
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next())
			{
				int entry_id = rs.getInt("entry_id");
				Timestamp time_stamp = rs.getTimestamp("time_stamp");
				int device_count = rs.getInt("device_count");
				devicesList.add(device_count);
				min = Collections.min(devicesList);
				max = Collections.max(devicesList);

				// double d1 = min;
				// double d2 = max;
				// double d3 = device_count;
				// double normalized = normalize(d1, d2, d3);
				// Color mapColor = colorInterpolation(Color.RED, Color.GREEN, normalized);
				// Display values

				// System.out.print("ID: " + entry_id);
				// System.out.print(", Time Stamp: " + time_stamp);
				// System.out.print(", Device Count: " + device_count);
				// System.out.print(", Normalized:" + normalized);
				// System.out.println(", Color RGB: " + String.valueOf(mapColor));

				// System.out.println(linearInterpolation(0, 1, normalized));
				count++;
			}

			// int min = Collections.min(devicesList);
			// int max = Collections.max(devicesList);
			lastCapture = devicesList.get(count - 1);
			System.out.println("min: " + min);
			System.out.println("max: " + max);
			System.out.println("Last Value: " + lastCapture);
			System.out.println();
			updateJSON(lastCapture,
					String.valueOf(colorInterpolation(Color.RED, Color.GREEN, normalize(min, max, lastCapture))));
			updateJSON();

			conn.close();
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
		}

	}

	private static double normalize(double min, double max, double value)
	{
		return (value - min) / (max - min);
	}

	public static double linearInterpolation(double start, double end, double normalizedValue)
	{
		return start + (end - start) * normalizedValue;
	}

	public static double sinInterpolation(double start, double end, double normalizedValue)
	{
		return (start + (end - start) * (1 - Math.cos(normalizedValue * Math.PI)) / 2);
	}

	public static Color colorInterpolation(Color color1, Color color2, double normal)
	{
		double inverse_percent = 1.0 - normal;
		int redPart = (int) (color1.getRed() * normal + color2.getRed() * inverse_percent);
		int greenPart = (int) (color1.getGreen() * normal + color2.getGreen() * inverse_percent);
		int bluePart = (int) (color1.getBlue() * normal + color2.getBlue() * inverse_percent);
		return new Color(redPart, greenPart, bluePart);
	}

	public static void updateJSON()
	{
		try
		{
			URL url = new URL("http://10.5.54.42/db.json");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json; utf-8");
			con.setRequestProperty("Accept", "application/json");
			con.setDoOutput(true);
			String jsonInputString = "{\"id\": \"4\", \"first_Name\":\"Sergey\",\"last_Name\":\"Kargopolov\", \"email\": \"fuckboi@dick.com\"}";
			try (OutputStream os = con.getOutputStream())
			{
				byte[] input = jsonInputString.getBytes("utf-8");
				os.write(input, 0, input.length);
			}

			try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8")))
			{
				StringBuilder response = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null)
				{
					response.append(responseLine.trim());
				}
				System.out.println(response.toString());
			}

		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	public static void updateJSON(int device_count, String RGB)
	{
		JSONObject obj = new JSONObject();
		ArrayList<Integer> values = parseRGB(RGB);
		obj.put("device_count", device_count);

		JSONArray list = new JSONArray();
		list.add(values.get(0));
		list.add(values.get(1));
		list.add(values.get(2));

		obj.put("RGB", list);

		try (FileWriter file = new FileWriter("F:\\My Stuff\\Coding\\test.json"))
		{
			file.write(obj.toJSONString());
			file.write("\n");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		System.out.print(obj);

	}

	public static ArrayList<Integer> parseRGB(String RGB)
	{
		ArrayList<Integer> values = new ArrayList<Integer>();
		String[] split = RGB.split(",");
		for (String s : split)
		{
			StringBuilder number = new StringBuilder("");
			char[] charsplit = s.toCharArray();
			for (char c : charsplit)
			{
				if (Character.isDigit(c))
					number.append(c);
			}
			String S = number.toString();
			values.add(Integer.valueOf(S));
		}
		return values;
	}

}
