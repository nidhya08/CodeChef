package menuFinder;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class CSVFileReader {

	
	public JSONObject readCSVIntoJSON(String fileName) {
		try (BufferedReader bReader = new BufferedReader(new FileReader(fileName))) {
		
		if(bReader != null) {

			String line = "";
			JSONObject hotelMenuJSON = new JSONObject();

			try {
				String prev = "";
				JSONObject priceObj = new JSONObject();

				while((line = bReader.readLine())!= null) {

					String[] csvString = line.replace(" ","").split(",");
					
					if(!prev.equals(csvString[0]) && priceObj.names()!=null) {

						hotelMenuJSON.accumulate(prev,priceObj );
						priceObj = new JSONObject();
					}
					String[] items = Arrays.copyOfRange(csvString,2, csvString.length);
					Arrays.sort(items);
					priceObj.put(String.join(",",items), csvString[1]);
					prev = csvString[0];
				}
				hotelMenuJSON.accumulate(prev, priceObj);

				return hotelMenuJSON;

			} catch (IOException ex) {

				System.err.println("Error while reading CSV file");
				ex.printStackTrace();

			} catch(JSONException jx) {

				System.err.println("Error while converting CSV to JSON");
				jx.printStackTrace();

			}

		}
		
		}catch(IOException ex) {
			System.err.print("Couldn't create reader object.");
			ex.printStackTrace();
			 
		}
		return null;

	}
}
