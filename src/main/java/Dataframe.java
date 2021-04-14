import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Dataframe {
	private ArrayList<ArrayList> data;
	private int maxElementLength = 0;
	

	public Dataframe(ArrayList<ArrayList> data) throws Exception {
		setData(data);
	}
	
	public Dataframe(String csv) 
	{
		ArrayList<ArrayList> columns = new ArrayList<ArrayList>();
		try {
			Scanner fileScanner = new Scanner(new File(csv));
			while(fileScanner.hasNextLine()) {
				int columnIdx = 0;
				String lineString = fileScanner.nextLine();
				Scanner rowScanner = new Scanner(lineString);
				rowScanner.useDelimiter("");
				Boolean escape = false;
				String el = "";
				int rowIdx = 0;
				while(rowScanner.hasNext()) {
					String c = rowScanner.next();
					if(c.charAt(0) == '"'){ // Escaping
						if(escape) // Add the escaped " char to the element
							el += c;
						escape = !escape;
					} else if(!escape && c.charAt(0) == ',') { // End of element
						if(el.length() > this.maxElementLength)	// Used for pretty printing
							this.maxElementLength = el.length() + 1;
						
						if(el.length() > 0 && el.charAt(el.length()-1) == '"') // Removes last " char of the element
							el = el.substring(0, el.length() - 1);		
												
						addElementAndCreateRowIfNecessary( columns, el, columnIdx);
						
						columnIdx++;						
						el = "";
					} else { // Element
						el += c;
					}
				}				
				addElementAndCreateRowIfNecessary( columns, el, columnIdx);
				rowIdx++;
				rowScanner.close();
			}
			fileScanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		this.data = columns;
	}
	
	//Method for constructor
	public void addElementAndCreateRowIfNecessary(ArrayList<ArrayList> columns, String el, int columnIdx)
	{
		boolean foundType = false;
		//Test if the column already exist
		if(columnIdx < columns.size()) {
			try {
				int elToAdd = Integer.parseInt(el); //If is Integer
				foundType = true;
				columns.get(columnIdx).add(Integer.parseInt(el));
			} catch(Exception e) {}//Nothing to do for the exception
			

			if(!foundType)
				try {
					float elToAdd = Float.parseFloat(el); //If is float
					foundType = true;
					columns.get(columnIdx).add(Float.parseFloat(el));
				} catch(Exception e) {}//Nothing to do for the exception
			
			if(!foundType)						  //Otherwise we add it like that
				columns.get(columnIdx).add(el);
		} else {

			try {
				int elToAdd = Integer.parseInt(el); //If is Integer
				foundType = true;
				columns.add(new ArrayList<Integer>());
				columns.get(columnIdx).add(Integer.parseInt(el));
			} catch(Exception e) {}//Nothing to do for the exception
			

			if(!foundType)
				try {
					float elToAdd = Float.parseFloat(el); //If is float
					foundType = true;
					columns.add(new ArrayList<Float>());
					columns.get(columnIdx).add(Float.parseFloat(el));
				} catch(Exception e) {}//Nothing to do for the exception
			
			if(!foundType)						  //Otherwise we add it like that
				columns.add(new ArrayList<String>());
				columns.get(columnIdx).add(el);							
		}
	}
	
	
	/**
	 * Getters, Setters
	 */
	
	//Getters
	private ArrayList<ArrayList> getData() {
		return data;
	}
	
	public Object getCell(int i, int j) {
		return this.data.get(i).get(j);
	}
	
	public int getNbLines() {
		return this.data.size();
	}
	
	public int getNbColumns() {
		return this.data.size() > 0 ? this.data.get(0).size() : 0;
	}
	
	//Setters
	private void setData(ArrayList<ArrayList> data) throws Exception 
	{		
		if(!Dataframe.isValidDataframe(data))
			throw new Exception("Erreur dans la structure de données du dataframe");
		
		this.data = data;
	}
	
	
	/*
	 * Validation Methods
	 */
	
	public static boolean isValidDataframe(ArrayList<ArrayList> dataframe)
	{
		boolean isValide = true;
		int j = 0;
		while(isValide && j < dataframe.size()) {
			ArrayList line = dataframe.get(j);
			int i = 0;
			String type = "";
			if(line.size() > 0)
				type = line.get(i).getClass().getName();
			
			while(isValide && i < line.size()) {
				if(line.get(i).getClass().getName() != type)
					isValide = false;
				i++;
			}
			j++;
		}
		return isValide;
	}
	
	
	/*
	 * toString Methods
	 */
	
	private String lineToString(int i){
		String s = "";
		for(Object element : data.get(i)) {
			String elementTmp = element.toString();
			while(elementTmp.length() < this.maxElementLength)
				elementTmp += " ";
			s += elementTmp;
		}
		s += "\t\n";
		return s;
	}
	
	public String toString() {
		String s = "";
		for(int i = 0; i < this.data.size(); i++) {
			s += lineToString(i);
		}
		return s;
	}
}
