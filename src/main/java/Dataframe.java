import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;

public class Dataframe {
	private HashMap<String, ArrayList> data;
	private int maxElementLength = 0;
	

	public Dataframe(HashMap<String, ArrayList> data) throws Exception {
		setData(data);
	}
	
	public Dataframe(String csv) 
	{
//		System.out.println("Debut de dataframe");
		HashMap<String, ArrayList> columns = new HashMap<String, ArrayList>();
		try {
			Scanner fileScanner = new Scanner(new File(csv));
			
			//List of the columns label
			ArrayList<String> labels = new ArrayList<String>();
			
			//Index of column and row
			int rowIdx = 0;
			
			while(fileScanner.hasNextLine()) {
				int columnIdx = 0;
				String lineString = fileScanner.nextLine();
//				System.out.println(lineString);
				
				Scanner rowScanner = new Scanner(lineString);
				rowScanner.useDelimiter("");
				Boolean escape = false;
				String el = "";
				
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
						
						if(rowIdx == 0) //If first row, it the label value
							labels.add(el.trim());
						else            //Otherwise we add it to the HashMap
							addElementAndCreateRowIfNecessary(columns, labels.get(columnIdx), el);
						
						columnIdx++;
						el = "";
					} else { // Element
						el += c;
					}
				}	
				
				if(el.length() > this.maxElementLength)	// Used for pretty printing
					this.maxElementLength = el.length() + 1;
				
				if(el.length() > 0 && el.charAt(el.length()-1) == '"') // Removes last " char of the element
					el = el.substring(0, el.length() - 1);	
				
				if(rowIdx == 0) //If first row, it the label value
					labels.add(el.trim());
				else if(!el.equals(""))          //Otherwise we add it to the HashMap
					addElementAndCreateRowIfNecessary(columns, labels.get(columnIdx), el);
				
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
	public void addElementAndCreateRowIfNecessary(HashMap<String, ArrayList> columns, String label, String el)
	{
		boolean foundType = false;
		//Remove unnecessary space
		el = el.trim();
		//Test if the column already exist
		if(columns.containsKey(label)) {
//			System.out.println("Ajout au label " + label + " l'élément " + el);
			try {
				int elToAdd = Integer.parseInt(el); //If is Integer
				foundType = true;
				columns.get(label).add(elToAdd);
			} catch(Exception e) {}//Nothing to do for the exception
			

			if(!foundType)
				try {
					float elToAdd = Float.parseFloat(el); //If is float
					foundType = true;
					columns.get(label).add(elToAdd);
				} catch(Exception e) {}//Nothing to do for the exception
			
			if(!foundType)						  //Otherwise we add it like that
				columns.get(label).add(el);
		} else {	
//			System.out.println("Création du label " + label);
//			System.out.println("Ajout au label " + el);
			
			try {
				int elToAdd = Integer.parseInt(el); //If is Integer
				foundType = true;
				columns.put(label, new ArrayList<Integer>());
				columns.get(label).add(elToAdd);
			} catch(Exception e) {}//Nothing to do for the exception
			

			if(!foundType)
				try {
					float elToAdd = Float.parseFloat(el); //If is float
					foundType = true;
					columns.put(label, new ArrayList<Float>());
					columns.get(label).add(elToAdd);
				} catch(Exception e) {}//Nothing to do for the exception
			
			if(!foundType)						  //Otherwise we add it like that
			{
				columns.put(label, new ArrayList<String>());
				columns.get(label).add(el);		
			}
		}
	}
	
	
	/**
	 * Getters, Setters
	 */
	
	//Getters
	private HashMap<String, ArrayList> getData() {
		return data;
	}
	
	public Object getCell(String label, int j) throws Exception
	{
		if(this.getData().containsKey(label) && this.getData().get(label).size() > j && j > 0)
			return this.getData().get(label).get(j);
		else
			throw new Exception("Le label ou la ligne " + j + " n'exsite pas");
		
	}
	
	public int getNbLines() {
		if(this.data.size() == 0)
			return 0;
		else {
			Object[] keys =  this.getData().keySet().toArray(); //Get the keys
//			for(int i = 0; i < 88; i++) {
//				System.out.print(this.data.get(keys[0]).get(i));
//				System.out.print(" - " + this.data.get(keys[1]).get(i));
//				System.out.println(" - " + this.data.get(keys[2]).get(i));
//			}
			return this.getData().get(keys[0]).size(); //Send the number of lines of the first column given that every column have the same amount of lines
		}
	}
	
	public int getNbColumns() {
		return this.getData().size();
	}
	
	//Setters
	private void setData(HashMap<String, ArrayList> data) throws Exception 
	{		
		if(!Dataframe.isValidDataframe(data))
			throw new Exception("Erreur dans la structure de données du dataframe");
		
		this.data = data;
	}
	
	
	/*
	 * Validation Methods
	 */
	
	public static boolean isValidDataframe(HashMap<String, ArrayList>  dataframe)
	{
		int j = 0;
		for(String key: dataframe.keySet()) {
			ArrayList line = dataframe.get(key);
			String type = "";	
			
			if(line.size() > 0)
				type = line.get(0).getClass().getName();
			
			
			for(Object o: line) {
				if(o.getClass().getName() != type)
					return false;
			}
			j++;
		}
		return true;
	}
	
	
	/*
	 * toString Methods
	 */
	
	private String lineToString(int i){
		String s = "";
		for(String key: this.getData().keySet()) {
			String elementTmp = "";
			try {
				elementTmp = this.getCell(key, i).toString();
			} catch(Exception e) {}
			
			while(elementTmp.length() < this.maxElementLength)
				elementTmp += " ";
			s += elementTmp;
		}
		s += "\t\n";
		return s;
	}
	
	private String labelsToString(){
		String s = "";
		for(String key: this.getData().keySet()) {
			String elementTmp = key;
			while(elementTmp.length() < this.maxElementLength)
				elementTmp += " ";
			s += elementTmp;
		}
		s += "\t\n";
		return s;
	}
	
	public String toString() {
		String s = "";
		s += labelsToString();
		
		for(int i = 0; i < this.getNbLines(); i++) {
			s += lineToString(i);
		}
		return s;
	}
}
