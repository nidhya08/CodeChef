package menuFinder;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
class Recommender {
		
	public static void main(String[] args){
		CSVFileReader csvFileReader = new CSVFileReader();
		Aggregator aggregate = new Aggregator();
		
		JSONObject jsonHotelMenu = csvFileReader.readCSVIntoJSON("Items.csv");
		JSONObject jsonAggregatePrice = aggregate.createAggregateMap(jsonHotelMenu);
		System.out.println(jsonHotelMenu);
		
	}
}