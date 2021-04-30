import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.HashMap;

public class Dataframe {
	/**
	 * The data of the class
	 */
	private HashMap<String, ArrayList> data;
	
	/**
	 * The max length of of all the value
	 */
	private int maxElementLength = 0;
	
	/**
	 * Create DataFrame from data structure
	 * @param data Dataframe data
	 * @throws Exception Throws an exception if the given data is not valid
	 */
	public Dataframe(HashMap<String, ArrayList> data) throws Exception {
		setData(data);
	}
	
	/**
	 * Create DataFrame from csv file
	 * @param csv The file path to the csv file
	 * @throws FileNotFoundException Throws an exception if the given file is not found
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
	 * Private method for constructor
	 */
	private void addElementAndCreateRowIfNecessary(HashMap<String, ArrayList> columns, String label, String el)
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
	 * Return the average of a column if it is a computable column
	 * @param label Label of the column
	 * @return The average of that column
	 * @throws Exception Throws an exception if the average cannot be computed
	 */
	public float getAverage(String label) throws Exception
	{
		if(!this.getData().containsKey(label))
			throw new Exception("The label " + label +  " does not exist in the data");
		
		float average = 0;
		
		if(this.getData().get(label).size() == 0)
			return 0;
		
		if(!this.isColumnComputable(label))
			throw new Exception("The label " + label +  " does not contain value that can be calculated");
		
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
	 * @param label Label of the column
	 * @return The minimum of that column
	 * @throws Exception Throws an exception if the minimum cannot be computed
	 */
	public float getMin(String label) throws Exception
	{
		if(!this.getData().containsKey(label))
			throw new Exception("The label " + label +  " does not exist in the data");
		
		if(this.getData().get(label).size() == 0)
			throw new Exception("The label " + label +  " does not containt value that can be calculated");
		
		if(!this.isColumnComputable(label))
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
	 * Return the max of a column
	 * @param label Label of the column
	 * @return The maximum of that column
	 * @throws Exception Throws an exception if the maximum cannot be computed
	 */
	public float getMax(String label) throws Exception
	{
		if(!this.getData().containsKey(label))
			throw new Exception("The label " + label +  " does not exist in the data");
		
		if(this.getData().get(label).size() == 0)
			throw new Exception("The label " + label +  " does not containt value that can be calculated");
		
		if(!this.isColumnComputable(label))
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
	
	/**
	 * Returns whether a column is computable or not
	 * @param label The label of the column
	 * @return True if the column is a computable, false otherwise
	 * @throws Exception Throws an exception if column does not exist
	 */
	public boolean isColumnComputable(String label) throws Exception
	{
		if(!this.getData().containsKey(label))
			throw new Exception("The label " + label +  " does not exist in the data");
		
		String[] numericTypes = {
				"java.lang.Float",
				"java.lang.Integer"
		};
			
		return Arrays.asList(numericTypes).contains(this.getData().get(label).get(0).getClass().getName());
	}
	
	
	/**
	 * Getters, Setters
	 * 
	 */
	
	/**
	 * @return the dataframe data
	 */
	private HashMap<String, ArrayList> getData() {
		return data;
	}
	
	/**
	 * Returns the value of one cell
	 * @param label The label of the column
	 * @param j The value of the line
	 * @return The value of the cell
	 * @throws Exception Throws an exception if the cell cannot be found (out of bounds or unknown label)
	 */
	public Object getCell(String label, int j) throws Exception
	{
		if(this.getData().containsKey(label) && this.getData().get(label).size() > j && j > 0)
			return this.getData().get(label).get(j);
		else
			throw new Exception("Le label ou la ligne " + j + " n'exsite pas");
		
	}
	
	/**
	 * @return The number of lines in a dataframe
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
	 * @return The number of columns in a dataframe
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
	
	/**
	 * @return The maximum length observed among all elements of the dataframe (used for pretty printing)
	 */
	private int getMaxElementLength() {
		return maxElementLength;
	}

	public void setMaxElementLength(int maxElementLength) {
		this.maxElementLength = maxElementLength;
	}
	
	
	/*
	 * Validation Methods
	 */

	/**
	 * Indicates whether some given data from a dataframe is valid or not
	 * @param dataframe Dataframe data
	 * @return If the data is valid
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
	 * Returns a subset of the dataframe
	 * @param i the line index to start from
	 * @param j the line index to stop at, if j is greater than the last line index, j is equal to the last index
	 * @return the subset
	 * @throws Exception Throws an exception if the subset interval isn't valid (i must be greater than j and 0)
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
	
	/**
	 * Returns a subset of the dataframe
	 * @param labels the labels to put in the subset
	 * @return the subset
	 * @throws Exception Throws an exception if the subset interval isn't valid (i must be greater than j and 0)
	 */
	public Dataframe subset(String...labels) throws Exception
	{
		for(String label: labels)
			if(!this.getData().containsKey(label))
				throw new Exception("The label " + label + " does not exist");
		
		HashMap<String, ArrayList> m = new HashMap<String, ArrayList>();
		
		for(String key: labels) {
			m.put(key, this.getData().get(key));						
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
	 * @param n the number of lines to print
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
	 * @param n the number of lines to print
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
