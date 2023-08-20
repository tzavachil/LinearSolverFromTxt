import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Splitter {
	private Checker checker;
	
	public Splitter() {
		checker = new Checker();
	}
	
	//removing "z=" if there is one
	public String removeZ(String line) {
		if(line.matches("[a-zA-z]+[=].*")) 
			line = line.replaceAll("([a-zA-z]+[=])", "");
		
		return line;
	}
	
	//splits a line into the right components
	public boolean uniSplit(String line, int checker, Solver solver, int row) {
		//checker 0 : input from splitFirstRow(String line);
		//checker 1 : input from splitSubjects(String line);
		
		line = line.replaceAll("\\s","");
		if(checker == 0)
			line = removeZ(line); //must remove "z=";
		line = replacePlus(line);
		
		//Signs part
		Pattern patternSign = Pattern.compile("[+-]");
		String newLine;
		Matcher matcherSign;
		if(checker == 0)
			matcherSign = patternSign.matcher(line);
		else {
			newLine = line.replaceAll("[<>=]+[-]*[0-9]+.*", "");
			matcherSign = patternSign.matcher(newLine);
		}
		
		//Digits part
		String stringPattern;
		if(checker == 0)
			stringPattern = "[-+][0-9]*[a-zA-z]+[0-9]*";
		else
			stringPattern = "[-+][0-9]*[a-zA-z]+[0-9]*|[<>=]+|[-+]*[0-9]+";
		Pattern patternDigit = Pattern.compile(stringPattern);
		Matcher matcherDigit = patternDigit.matcher(line);
		
		//counters
		int symbolsCount = 0; //counts the symbols
		int splitsCount = 0; //counts splits of the String
		
		while(matcherSign.find()) {
			symbolsCount++;
		}
		
		while(matcherDigit.find()) {
			splitsCount++;
			if(checker == 0)
				commonSaveForAandC(matcherDigit.group(),solver,1,0);
			else
				saveValue(matcherDigit.group(),solver,row);
		}

		boolean flag = false; //flag that searching for an error
		int secondValue;

		//the pattern from splitSubjects has 2 more splits: the <= part and one number
		if(checker == 0)
			secondValue = splitsCount;
		else
			secondValue = splitsCount-2;
		
		if(symbolsCount == secondValue) {
			flag = true;
		}
		
		return flag;
	}
	
	//splits the first row into the right components
	public boolean splitFirstRow(String line, Solver solver) {
		return uniSplit(line,0,solver,-1);
	}
	
	//splits the subjects rows into the right components
	public boolean splitSubjects(String line, Solver solver,int row) {
		return uniSplit(line,1,solver,row);
	}
	
	//adds the plus symbol where necessary
	public String replacePlus(String line) {
		String newLine = line;
		String plusPos = line.substring(0,1);
				
		if(!plusPos.equals("-")) {
			newLine = "+" + newLine;
		}
		
		return newLine;
	}
	
	//saves each value in the correct position 
	public void saveValue(String data, Solver solver, int row) {
		switch(checker.checkForPos(data)) {
			case 0 : 
				//data --> table Eqin
				saveEqin(data,solver);
				break;
			case 1 : 
				//data --> table A
				commonSaveForAandC(data,solver,0,row);
				break;
			case 2 : 
				//data --> table b
				saveTableB(data,solver);
				break;
			default : 
				System.out.println("Error");
				System.exit(0);
				break;
			
		}
	}
	
	//sends the right data on the solver to save them into the Eqin List
	public void saveEqin(String data,Solver solver) {
		switch(data) {
			case "<=" :
				solver.addToEqin(-1);
				break;
			case "=" :
				solver.addToEqin(0);
				break;
			case ">=" :
				solver.addToEqin(1);
				break;
			default :
				System.out.println("Error");
				System.exit(0);
				break;
		}
	}
	
	//sends the right data on the solver to save them into the A table 
	public void saveTableA(Solver solver, double number, String variable, int row) {
		solver.addToA(number, variable, row);
		
	}
	
	//sends the right data on the solver to save them into the b List
	public void saveTableB(String data,Solver solver) {
		solver.addToB(Double.parseDouble(data));
	}
	
	//sends the right data on the solver to save them into the c List
	public void saveTableC(Solver solver, double number, String variable) {
		solver.addToC(number, variable);
	}
	
	//divide the number from the variable on some data
	public void commonSaveForAandC(String data,Solver solver, int checker, int row) {
		// checker = 0 : inputs for table A
		// checker = 1 : inputs for table C
		
		saveTableVar(data,solver);
		
		Pattern pattern = Pattern.compile("[+-][0-9]*|[a-zA-Z]+[0-9]*");
		Matcher matcher = pattern.matcher(data);
		
		double number = 0.0;
		String variable = null;
		
		while(matcher.find()) {
			if(!matcher.group().matches("[a-zA-z]+[0-9]*")) {
				if(matcher.group().matches("[+-]")) {
					//+1, -1 part
					number = Double.parseDouble(matcher.group() + "1");
				}
				else {
					//normal part
					number = Double.parseDouble(matcher.group());
				}
			}
			else
				variable = matcher.group();
		}
		
		if(checker == 0) {
			saveTableA(solver, number, variable, row);
		}
		else
			saveTableC(solver, number, variable);
	}
	
	//sends the right variables on the solver to save them into the var List
	public void saveTableVar(String data,Solver solver) {
		Pattern pattern = Pattern.compile("[+-][0-9]*|[a-zA-Z]+[0-9]*");
		Matcher matcherVar = pattern.matcher(data);

		while(matcherVar.find()) {
			if(matcherVar.group().matches("[a-zA-Z]+[0-9]*")) {
				solver.addToVar(matcherVar.group());
			}
		}
	}

}
