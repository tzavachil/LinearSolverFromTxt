
public class Checker {
	
	public Checker() {
		
	}
	
	//checking if a string equals 'max' or 'min' and return a boolean value
	public boolean checkMaxMin(String maxmin) {
		boolean flag = ((maxmin.equalsIgnoreCase("max")) || (maxmin.equalsIgnoreCase("min")));
				
		return flag;
	}
	
	//checking if a string equals 'st' or 's.t.' or 'subject' and return a boolean value
	public boolean checkSt(String st) {
		boolean flag;
		
		try
		{
			flag = ((st.equalsIgnoreCase("st")) || (st.equalsIgnoreCase("s.t.")) || (st.equalsIgnoreCase("subject")));	
		}
		catch(NullPointerException e) {
			flag = false;
		}
		
		
		
		return flag;
	}
	
	//checking the content of the string and returns a integer depending on where it needs to be stored
	public int checkForPos(String data) {
		//pos 0 : data --> table Eqin
		//pos 1 : data --> table A
		//pos 2 : data --> table b
		
		int pos = -1;
		
		if(isSymbol(data))
			pos = 0;
		else if(data.matches("[+-][0-9]*[a-zA-Z]+[0-9]*"))
			pos = 1;
		else
			pos = 2;
		
		return pos;
	}
	
	//checking if a string is one the '<=','=','>=' symbols
	public boolean isSymbol(String data) {
		return ((data.equals("<=")) || (data.equals("=")) || (data.equals(">=")));
	}
	
	//checking the right format of the line
	public boolean checkLine(String line) {
		boolean flag = false;
		
		line = line.replaceAll("\\s","");
		
		String rightFormat = "([-+]*[0-9]*[a-zA-Z]+[0-9]*)+[<>=]+[0-9]+[,]?";
		
		if((line.matches(rightFormat) || line.equalsIgnoreCase("end")))
			flag = true;
		
		return flag;
	}
}
