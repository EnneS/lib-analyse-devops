import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.HashMap;

public class Dataframe {
	/**
	 * The data oof the class
	 */
	private HashMap<String, ArrayList> data;
	
	/**
	 * The max length of of all the value
	 */
	private int maxElementLength = 0;
	
	/**
	 * Create DataFrame from data structure
	 * @param data
	 * @throws Exception
	 */
	public Dataframe(HashMap<String, ArrayList> data) throws Exception {
		setData(data);
	}
	
	/**
	 * Create DataFrame from cvs file
	 * @param csv the file path
	 * @throws FileNotFoundException
	 */
	public Dataframe(String csv)  throws FileNotFoundException
	{
		HashMap<String, ArrayList> columns = new HashMap<String, ArrayList>();
		
		Scanner fileScanner = new Scanner(new File(csv));
		
		//List of the columns label
		ArrayList<String> labels = new ArrayList<String>();
		
		//Index of column and row
		int rowIdx = 0;
		
		while(fileScanner.hasNextLine()) {
			int columnIdx = 0;
			String lineString = fileScanner.nextLine();
			
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
			else          //Otherwise we add it to the HashMap
				addElementAndCreateRowIfNecessary(columns, labels.get(columnIdx), el);
			
			rowIdx++;
			rowScanner.close();
		}
		fileScanner.close();
		if(rowIdx == 1) {
			for(String s: labels) {
				columns.put(s, new ArrayList());
			}
		}
		this.data = columns;
	}
	
	/**
	 * Method for constructor
	 */
	public void addElementAndCreateRowIfNecessary(HashMap<String, ArrayList> columns, String label, String el)
	{
		boolean foundType = false;
		//Remove unnecessary space
		el = el.trim();
		//Test if the column already exist
		if(columns.containsKey(label)) {
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
	 * Stats Methods
	 */
	
	/**
	 * Return the average of a column if if is a calculable colomn
	 * @param label
	 * @return
	 * @throws Exception
	 */
	public float getAverage(String label) throws Exception
	{
		if(!this.getData().containsKey(label))
			throw new Exception("The label " + label +  " does not exist in the data");
		
		float average = 0;
		
		if(this.getData().get(label).size() == 0)
			return 0;
		
		if(!this.isCalculableColumn(label))
			throw new Exception("The label " + label +  " does not containt value that can be calculated");
		
		for(int i = 0; i < this.getData().get(label).size(); i++) {
			if(this.getData().get(label).get(i).getClass().getName().equals("java.lang.Float"))
				average += (Float) this.getData().get(label).get(i);
			else
				average += ((Integer) this.getData().get(label).get(i)).floatValue();
		}
		
		return average/this.getData().get(label).size();
	}
	
	/**
	 * Return the min of a column
	 * @param label of the column
	 * @return min
	 * @throws Exception
	 */
	public float getMin(String label) throws Exception
	{
		if(!this.getData().containsKey(label))
			throw new Exception("The label " + label +  " does not exist in the data");
		
		if(this.getData().get(label).size() == 0)
			throw new Exception("The label " + label +  " does not containt value that can be calculated");
		
		if(!this.isCalculableColumn(label))
			throw new Exception("The label " + label +  " does not containt value that can be calculated");

		float min;
		if(this.getData().get(label).get(0).getClass().getName().equals("java.lang.Float"))
			min = (Float) this.getData().get(label).get(0);
		else
			min = (Integer) this.getData().get(label).get(0);
		
		for(int i = 1; i < this.getData().get(label).size(); i++) {
			if(this.getData().get(label).get(i).getClass().getName().equals("java.lang.Float")) {
				float currentFloat = (Float) this.getData().get(label).get(i);
				if(currentFloat < min)
					min = currentFloat;
			} else {
				Integer currentInt = (Integer) this.getData().get(label).get(i);
				if(currentInt < min)
					min = currentInt.floatValue();
			}
			
		}	
		return min;		
	}
	
	/**
	 * Return the min of a column
	 * @param label of the column
	 * @return min
	 * @throws Exception
	 */
	public float getMax(String label) throws Exception
	{
		if(!this.getData().containsKey(label))
			throw new Exception("The label " + label +  " does not exist in the data");
		
		if(this.getData().get(label).size() == 0)
			throw new Exception("The label " + label +  " does not containt value that can be calculated");
		
		if(!this.isCalculableColumn(label))
			throw new Exception("The label " + label +  " does not containt value that can be calculated");

		float max;
		if(this.getData().get(label).get(0).getClass().getName().equals("java.lang.Float"))
			max = (Float) this.getData().get(label).get(0);
		else
			max = (Integer) this.getData().get(label).get(0);
		
		for(int i = 1; i < this.getData().get(label).size(); i++) {
			if(this.getData().get(label).get(i).getClass().getName().equals("java.lang.Float")) {
				float currentFloat = (Float) this.getData().get(label).get(i);
				if(currentFloat > max)
					max = currentFloat;
			} else {
				Integer currentInt = (Integer) this.getData().get(label).get(i);
				if(currentInt > max)
					max = currentInt.floatValue();
			}
			
		}	
		return max;		
	}
	
	
	public boolean isCalculableColumn(String label) throws Exception
	{
		if(!this.getData().containsKey(label))
			throw new Exception("The label " + label +  " does not exist in the data");
		
		String[] numericTypes = {
				"java.lang.Float",
				"java.lang.Integer"
			};
			
		if(!Arrays.asList(numericTypes).contains(this.getData().get(label).get(0).getClass().getName()))
			return false;
		else
			return true;	
		
	}
	
	
	/**
	 * Getters, Setters
	 * 
	 */
	
	/**
	 * @return the data
	 */
	private HashMap<String, ArrayList> getData() {
		return data;
	}
	
	/**
	 * Return the value of one cell
	 * @param label the label of the column for the hashmap
	 * @param j the value of the line
	 * @return the value of the cell
	 * @throws Exception
	 */
	public Object getCell(String label, int j) throws Exception
	{
		if(this.getData().containsKey(label) && this.getData().get(label).size() > j && j > 0)
			return this.getData().get(label).get(j);
		else
			throw new Exception("Le label ou la ligne " + j + " n'exsite pas");
		
	}
	
	/**
	 * @return the number of line
	 */
	public int getNbLines() {
		if(this.data.size() == 0)
			return 0;
		else {
			Object[] keys =  this.getData().keySet().toArray(); //Get the keys
			return this.getData().get(keys[0]).size(); //Send the number of lines of the first column given that every column have the same amount of lines
		}
	}
	
	/**
	 * @return the number of column
	 */
	public int getNbColumns() {
		return this.getData().size();
	}
	
	private void setData(HashMap<String, ArrayList> data) throws Exception 
	{		
		if(!Dataframe.isValidDataframe(data))
			throw new Exception("Erreur dans la structure de données du dataframe");
		
		this.data = data;
	}
	
	public int getMaxElementLength() {
		return maxElementLength;
	}

	public void setMaxElementLength(int maxElementLength) {
		this.maxElementLength = maxElementLength;
	}
	
	
	/*
	 * Validation Methods
	 */

	/**
	 * @param dataframe
	 * @return if the dataframe isValide
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
				if(o.getClass().getName() != type && !type.equals("".getClass().getName())) {
					return false;
				}
			}
			j++;
		}
		return true;
	}
	
	/**
	 * Return a subset of the dataframe
	 * @param i the line to start
	 * @param j the line to stop, if j > nbLine return until the end
	 * @return the subset
	 * @throws Exception 
	 */
	public Dataframe subset(int i, int j) throws Exception
	{
		if(i < 0 || i > this.getNbLines() || i >= j)
			throw new Exception("The from value must be greater than 0 and less than the number of lines");
		
		if(j > this.getNbLines())
			j = this.getNbLines();
		
		HashMap<String, ArrayList> m = new HashMap<String, ArrayList>();
		
		for(String key: this.getData().keySet()) {
			ArrayList newArrayList = (ArrayList) this.getData().get(key).clone();
			newArrayList.clear();
			for(int k = i; k < j; k++)
				newArrayList.add(this.getData().get(key).get(k));
			m.put(key, newArrayList);						
		}
		
		Dataframe subD = new Dataframe(m);
		
		subD.setMaxElementLength(this.getMaxElementLength());
		
		return subD;
	}
	
	/*
	 * toString Methods
	 */
	
	/**
	 * Return one line in string format
	 * @param i the line to return
	 * @return the string
	 */
	private String lineToString(int i){
		String add = this.getAddForToString();
		String s = "" + i + ": ";
			for(int j = s.length(); j < add.length(); j++) {
				s = " " + s;
			}
		for(String key: this.getData().keySet()) {
			String elementTmp = "";
			try {
				elementTmp += this.getCell(key, i).toString();
			} catch(Exception e) {}
			
			while(elementTmp.length() < this.getMaxElementLength())
				elementTmp += " ";
			s += elementTmp;
		}
		s += "\t\n";
		return s;
	}
	
	/**
	 * Return the labels in string format
	 * @return the string
	 */
	private String labelsToString(){		
		String s = this.getAddForToString();
		for(String key: this.getData().keySet()) {
			String elementTmp = key;
			while(elementTmp.length() < this.getMaxElementLength())
				elementTmp += " ";
			s += elementTmp;
		}
		s += "\t\n";
		return s;
	}
	
	/**
	 * Return the good number of white space in order to pretty print the number of line
	 * @return white space in string
	 */
	private String getAddForToString()
	{
		String s = "";
		String nbLine = "" + this.getNbLines() + ": ";
		for(int i = 0; i < nbLine.length(); i++)
			s += " ";
		
		return s;
	}
	
	/**
	 * Return the n first lines in string format
	 * @param n the number of line to print
	 * @return the string
	 */
	public String nFirstLineToString(int n)
	{
		String s = "";
		s += labelsToString();
		
		int max = n;
		if(this.getNbLines() < max)
		{
			max = this.getNbLines();
		}
		
		for(int i = 0; i < n; i++) {
			s += lineToString(i);
		}
		
		return s;
	}

	/**
	 * Return the n last lines in string format
	 * @param n the number of line to print
	 * @return the string
	 */
	public String nLastLineToString(int n)
	{
		String s = "";
		s += labelsToString();
		
		int i = this.getNbLines() - n;
		if(i < 0)
			i = 0;
		
		for(int idx = i; idx < this.getNbLines(); idx++) {
			s += lineToString(idx);
		}
		
		return s;
	}

	/**
	 * Return the data in string format
	 * @return the string
	 */
	public String toString() {
		String s = "";
		s += labelsToString();
		
		for(int i = 0; i < this.getNbLines(); i++) {
			s += lineToString(i);
		}
		return s;
	}
}
