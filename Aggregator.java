package menuFinder;

import org.json.JSONObject;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class Aggregator {

	private String[] tempCombo;
	private String[] tempCostCombo;

	@SuppressWarnings("unchecked")
	public HashSet<String> findUniqueItems(JSONObject jsonMenu) {

		HashSet<String> itemsSet = new HashSet<>();
		Iterator<String> items = jsonMenu.keys();
		while(items.hasNext()) {
			String combo = items.next();
			for(String item : combo.split(",")) {
				itemsSet.add(item);
			}
		}

		return itemsSet;
	}

	public HashMap<String,Double> createAggregateForHotel(HashSet<String> items, JSONObject jsonMenu) throws JSONException {
		HashMap<String,Double> itemMap = new HashMap<String,Double>();
		
		String[] itemsArray = convertObjToStrArray(items.toArray());
		Arrays.sort(itemsArray);
		String[] tempArr = new String[itemsArray.length-1];
		
		for(int i=0;i<itemsArray.length;i++) {
			itemMap.put(itemsArray[i], getMinCostForIndividual(itemsArray[i], jsonMenu));
			if(i+1 <itemsArray.length)
				tempArr[i] = itemsArray[i];
			
		}

		setTempCombo(tempArr);
		for(int i = 0; i<itemsArray.length-1; i++){
			
			//tempCombo = generatePossibleCombos(tempCombo,itemsArray,i+1,itemsArray.length-i);
			tempArr = generatePossibleCombos(tempCombo,itemsArray,i+1,tempCombo.length);
			for(String combo: tempArr) {
				//System.out.println(combo);
				if(combo!=null)
					itemMap.put(combo, getMinCostForCombo(combo, itemMap, jsonMenu));
			}

		}
		System.out.println(itemMap);
		return itemMap;
	}

	public String[] convertObjToStrArray(Object[] objArray) {
		
		String[] strArray = new String[objArray.length];
		for(int i=0;i<objArray.length;i++) {
			strArray[i] = objArray[i].toString();
		}
		return strArray;
	}
	
	@SuppressWarnings("unchecked")
	public Double getMinCostForIndividual(String item, JSONObject jsonMenu) {
		Iterator<String> menuKeys = jsonMenu.keys();
		Double minCost = null;


		while(menuKeys.hasNext()) {
			String key = menuKeys.next();
			try {
				if(key.contains(item) && (minCost==null || minCost > jsonMenu.getDouble(key))) {

					minCost = jsonMenu.getDouble(key);

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}


		return minCost;
	}

	public Double getMinCostForCombo(String combo,HashMap<String,Double> itemMap,JSONObject jsonMenu) throws JSONException {

		ArrayList<Double> costList = new ArrayList<Double>();

		String[] items = combo.split(",");
		Double cost = 0.0;

		for(String item : items) {
			cost += itemMap.get(item);
		}
		costList.add(cost);
		Arrays.sort(items);
		setTempCostCombo(items);

		for(int i = 0; i<items.length-1; i++){

			costList.addAll(getCostForCombo(getTempCostCombo(),items,jsonMenu,i+1,items.length-i));
		}

		return Collections.min(costList);
	}

	@SuppressWarnings("unchecked")
	public ArrayList<Double> getCostForCombo(String[] itemsArr1, String[] itemsArr2, JSONObject jsonMenu, int start, int end) throws JSONException{
		int k=0;
		ArrayList<Double> comboCost = new ArrayList<Double>();
		String[] tempArr = new String[itemsArr1.length*2];

		for(int i = 0; i<end; i++){
			for(int j = i+start; j<itemsArr2.length; j++){
				String temp = itemsArr1[i]+","+itemsArr2[j];
				tempArr[k++] = temp;

				Iterator<String> keys = jsonMenu.keys();	
				while(keys.hasNext()) {
					String key = keys.next();
					
					if(key.contains(temp) && (jsonMenu.getDouble(key)!= 0.0)) {
						comboCost.add(jsonMenu.getDouble(key));
						System.out.println("match: "+jsonMenu.getDouble(key)+"   "+key);
					}
				}

			}
		}
		setTempCostCombo(tempArr);
//		System.out.println(comboCost);
		return comboCost;
	}

	public String[] getTempCombo() {
		return tempCombo;
	}

	public void setTempCombo(String[] temp) {
		this.tempCombo = temp;
	}
	
	
	public String[] getTempCostCombo() {
		return tempCostCombo;
	}

	public void setTempCostCombo(String[] tempCostCombo) {
		this.tempCostCombo = tempCostCombo;
	}

	public String[] generatePossibleCombos(String[] itemsArr1,String[] itemsArr2, int start, int end){

		int k = 0;
		int tempLength = getCombination(itemsArr2.length,start+1);
		String[] temp = new String[tempLength];
		ArrayList<String> tempDiscard = new ArrayList<String>();
		
		for(int i = 0; i<end; i++){

			int x = ((i + start)>= itemsArr2.length?(i + --start):(i+start));
			
			for(int j = x; j<itemsArr2.length; j++){
			
				temp[k++] = itemsArr1[i]+","+itemsArr2[j];
				
				if(j+1 != itemsArr2.length)
					tempDiscard.add(temp[k-1]);
				
				
			}
			
		}
		
		
		setTempCombo(convertObjToStrArray(tempDiscard.toArray()));
		return temp;
	}
	public int getCombination(int n, int r) {
		Digits num = (x) -> {
			int f=1;
			for(int i=2;i<=x;i++)
				f *= i;
			return f;
		};
		return (num.factorial(n)/(num.factorial(r)*num.factorial(n-r)));
	}
	

	public JSONObject createAggregateMap(JSONObject jsonMenu) {

		JSONObject jsonCollective = new JSONObject();
		Iterator<?> jsonIterator = jsonMenu.keys();
		try {
			while(jsonIterator.hasNext()){
				
				String restaurentName = jsonIterator.next().toString();
				JSONObject jsonHotelMenu = (JSONObject) jsonMenu.get(restaurentName);
				HashSet<String> uniqueItems = findUniqueItems(jsonHotelMenu);
				HashMap<String,Double> aggregateMap = createAggregateForHotel(uniqueItems, jsonHotelMenu);
				//jsonCollective.put(restaurentName, aggregateMap);
			}
		} catch (JSONException e) {

			e.printStackTrace();
		}

		return jsonCollective;
		
	}
}
