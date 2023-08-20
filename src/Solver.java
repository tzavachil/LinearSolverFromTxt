import java.util.ArrayList;

public class Solver {
	private int maxmin;
	private int varCount = 0;
	private double[][] a;
	private ArrayList<Double> b;
	private ArrayList<Double> c;
	private ArrayList<Integer> Eqin;
	private ArrayList<String> var;
	
	
	public Solver() {
		this.b = new ArrayList<>();
		this.c = new ArrayList<>();
		this.Eqin = new ArrayList<>();
		this.var = new ArrayList<>();
	}

	//sets value for max or min
	public void setMaxmin(int maxmin) {
		this.maxmin = maxmin;
	}
	
	public int get2DTableRows(double[][] table) {
		int rows = 0;
		
		for(double[] row : table) {
			rows ++;
		}
		
		return rows;
	}
	
	//creates an empty table A filled with 0
	public void createTableA(int rows, int col) {
		this.a = new double[rows][col];
	}
	
	public void extendTableA() {
		//initial dimensions of table A
		int tempRows = get2DTableRows(this.a);
		int tempCol = this.varCount-1;
		
		double[][] tempA = new double[tempRows][tempCol];
		tempA = this.a;
		createTableA(tempRows,(tempCol+1));
		for(int i=0; i<tempRows; i++) {
			for(int j=0; j<tempCol; j++) {
				this.a[i][j] = tempA[i][j];
			}
			this.a[i][tempCol] = 0.0;
		}
	}
	
	//adds numbers on the table A in the 'row' row. Variable is needed to find the column for the number.
	public void addToA(double number, String variable, int row) {
		int col = searchPos(variable);
		
		try {
			a[row][col] = number;
		}
		catch(ArrayIndexOutOfBoundsException e) {
			//new variable on subjects
			extendTableA();
			a[row][col] = number;		
		}
	}

	//adds value on the List Eqin
	public void addToEqin(int value) {
		this.Eqin.add(value);
	}
	
	//adds numbers on the List b
	public void addToB(double number) {
		this.b.add(number);
	}

	//adds the number into the right column (variable) in the List c
	public void addToC(double number, String variable) {
		int pos = searchPos(variable);
		
		this.c.remove(pos);
		this.c.add(pos,number);
	}
	
	//searches the right column for the 'variable' variable in the var List
	public int searchPos(String variable) {
		int pos = -1;
		
		for(String var : this.var) {
			pos++;
			if(var.equals(variable)) {
				break;
			}
		}
		
		return pos;
	}
	
	/* adds values on the var list if there is not already in there. 
	 * Also adds the same amount of 0 in the c list
	 * */
	public void addToVar(String value) {
		if(!var.contains(value)) {
			this.var.add(value);
			this.c.add(0.0);
			this.varCount++;
		}
	}
	
	//return the number of the variable on our problem
	public int getVarCount() {
		return this.varCount;
	}

	public String printOutput() {
		String output;
		
		output = "MinMax=" + this.maxmin + "\n\n";
		output = output + transformToString("c",this.c,0);
		output = output + transformAToString(this.a);
		output = output + transformToString("b",this.b,0);
		output = output + transformToString("Eqin", this.Eqin,1);
		
		return output;
	}
	
	@SuppressWarnings("unchecked")
	public String transformToString(String listName, ArrayList<?> aList, int checker) {
		//checker = 0 : input from ArrayList<Double>
		//checker = 1 : input from ArrayList<Integer>
		
		String line = listName + "=[";
		
		switch(checker) {
			case 0 :
				for(Double data : (ArrayList<Double>)aList)
					line = line + " " + data + "\n";
				break;
			case 1 :
				for(Integer data : (ArrayList<Integer>)aList)
					line = line + " " + data + "\n";
				break;
		}
		
		line = line.substring(0, line.length()-1) + "]\n\n"; //the substring is because we don't want the last new line character
		
		return line;
	}
	
	public String transformAToString(double[][] table) {
		String line = "A=[";
		
		for(double[] row : table) {
			String stringRow = "";
			for(double num : row) 
				stringRow = stringRow + " " + num;
			line = line + stringRow + "\n";
		}
		
		line = line.substring(0, line.length()-1) + "]\n\n"; //the substring is because we don't want the last new line character
		
		return line;
	}
	
}
