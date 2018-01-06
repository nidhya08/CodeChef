import java.util.Scanner;

class ChefAndCake {
public int calculateCost(char[][] cakeMatrix, int rowCount, int colCount){
	int cost1 = 0,cost2 = 0;
	for(int i=0;i<rowCount;i++){
		for(int j=0;j<colCount;j++){
			if((i+j)%2 == 0){
				if(cakeMatrix[i][j] == 'R')
					cost1 +=5;
			} else {
				if(cakeMatrix[i][j] == 'G')
					cost1 += 3;
			}
			if((i+j)%2 != 0){
				if(cakeMatrix[i][j] == 'R')
					cost2 +=5;
			} else {
				if(cakeMatrix[i][j] == 'G')
					cost2 += 3;
			}
		}
	}
	return (cost1>cost2?cost2:cost1);
}
public static void main(String[] args){
	Scanner sc = new Scanner(System.in);
	ChefAndCake chefCake = new ChefAndCake();
	
	int testCase = sc.nextInt();
	int[] minCost = new int[testCase];
	int count = 0;
	
	while(testCase-- >0){
		
		int row = sc.nextInt();
		int col = sc.nextInt();
		char[][] cakeMat = new char[row][col];
		
		for(int i =0; i<row;i++){
			String tempRow = sc.next();
			for(int j=0;j<col;j++)
				cakeMat[i][j] = tempRow.charAt(j);
		}
		
		minCost[count++] = chefCake.calculateCost(cakeMat,row,col);
		
	}
	
	for(int i=0;i<minCost.length;i++)
		System.out.println(minCost[i]);
}
}