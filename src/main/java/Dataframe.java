import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Dataframe {
	private ArrayList<ArrayList<String>> data;
	private int maxElementLength = 0;

	public Dataframe(ArrayList<ArrayList<String>> data) {
		setData(data);
	}
	
	public Dataframe(String csv) {
		ArrayList<ArrayList<String>> lines = new ArrayList<ArrayList<String>>();
		try {
			Scanner fileScanner = new Scanner(new File(csv));
			while(fileScanner.hasNextLine()) {
				ArrayList<String> line = new ArrayList<String>();
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
						line.add(el);
						el = "";
					} else { // Element
						el += c;
					}
				}
				line.add(el); // Last element of the line (last column)
				lines.add(line);
				rowScanner.close();
			}
			setData(lines);
			fileScanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private ArrayList<ArrayList<String>> getData() {
		return data;
	}
	
	private void setData(ArrayList<ArrayList<String>> data) {
		this.data = data;
	}
	
	private String lineToString(int i){
		String s = "";
		for(String element : data.get(i)) {
			String elementTmp = element;
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
	
	public String getCell(int i, int j) {
		return this.data.get(i).get(j);
	}
	
	public int getNbLines() {
		return this.data.size();
	}
	
	public int getNbColumns() {
		return this.data.size() > 0 ? this.data.get(0).size() : 0;
	}
}
