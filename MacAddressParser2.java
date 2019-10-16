import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;


public class MacAddressParser
{
        public static void main(String[] args) throws Exception
        {
                String fileLocation = args[0];
                File file = new File(fileLocation);

                BufferedReader br = new BufferedReader(new FileReader(fileLocation));

                String line;
		ArrayList<String> timeStamps = new ArrayList<String>();
                ArrayList<String> uniqueMac = new ArrayList<String>();
                while ((line = br.readLine()) != null)
                {
                        String[] spaceDelimited = line.split(" ");
                       	String timeStamp = spaceDelimited[0];
			String MAC = spaceDelimited[2];
                        if(!uniqueMac.contains(MAC))
			{
                        	timeStamps.add(timeStamp);
				uniqueMac.add(MAC);
			}
                }

                String fileName = args[1];
                BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
		for(int i = 0; i < uniqueMac.size(); i++)
                {
                        System.out.println(timeStamps.get(i) + " " +  uniqueMac.get(i));
                        writer.write(timeStamps.get(i) + " " +  uniqueMac.get(i) + "\n");
                }
		System.out.println("Count: " + uniqueMac.size());
                writer.close();
                br.close();
                System.exit(0);

        }
}
