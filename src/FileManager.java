import java.io.*;

public class FileManager {
	private Parser parser;
	private Solver solver;
	private Checker checker;
	private boolean error;
	private boolean flag;
	
	public FileManager() {
		this.parser = new Parser();
		this.solver = new Solver();
		this.checker = new Checker();
		this.flag = false;
		
		this.error = this.fileOpen("LP-1.txt"); //opens the input file
		this.fileStore(this.error); //stores the data on the output file
	}
	
	//opens a file and reads the data from it
	public boolean fileOpen(String fileName) {
		error = false;
		
		File fileInput = new File(fileName);
		System.out.println("....processing data...."); //just a message
		int rows = countRows(fileInput);
		try {
			FileReader r = new FileReader(fileInput);
			BufferedReader reader = new BufferedReader(r);
			String nextLine = reader.readLine(); //first row with min/max
			flag = parser.getFirstLine(nextLine,solver);
			if(flag) {
				nextLine = reader.readLine(); //second row with st
				flag = parser.getSecondLine(nextLine);
				solver.createTableA(rows, solver.getVarCount());
				if(flag) {
					int currRow = 0; //current row of sts
					while((nextLine = reader.readLine()) != null) {
						flag = checker.checkLine(nextLine);
						if(flag) {
							flag = parser.getLine(nextLine,solver,currRow);
							currRow++;
						}
						else {
							error = true;
							break;
						}
					}
				}
				else {
					System.out.println("No st/s.t./subject word found");
				}
			}
			else { 
				System.out.println("No max/min word found");
			}
			reader.close();
			r.close();
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}		
		catch (IOException i) {
			i.printStackTrace();
		}
		
		return error;
	}
	
	//reads a file and counts the rows from the second (st one) onwards
	public int countRows(File fileIn) {
		Checker checker = new Checker();
		
		int rows = -1; //from -1 because there is the 'end' line in the end
		
		try {
			FileReader r = new FileReader(fileIn);
			BufferedReader br = new BufferedReader(r);
			
			String line = br.readLine();

			while(!checker.checkSt(line)) 
				line = br.readLine();
				
			while((line = br.readLine()) != null)
				rows++;
			br.close();
			r.close();
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch(IOException i) {
			i.printStackTrace();
		}
		
		return rows;
	}
	
	//prints the output on another file
	public void fileStore(boolean error) {
		//error = false : no error
		//error = true : error
		
		try {
			File fileOut = new File("LP-2.txt");
			FileWriter writer = new FileWriter(fileOut);
			if(!error)
				writer.write(solver.printOutput());
			else
				writer.write("Error");
			System.out.println("Ouput is on output.txt file.");
			writer.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}

}
