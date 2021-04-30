import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;
import org.junit.rules.ExpectedException;

public class DataframeTest {
	public Dataframe d;
	
	@Before
	public void setUp() throws Exception {
		d = new Dataframe("src/test/resources/deniro.csv");
	}

	@After	
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testSize() {
		assertEquals(87, d.getNbLines());
		assertEquals(5, d.getNbColumns());
	}	
	
	@Test
	public void testWrondFilename()
	{
		try {
			d = new Dataframe("src/test/resources/deniros.csv");
			fail();
		} catch(Exception e) {}
	}
	
	@Test
	public void testEmptyFile()
	{
		try {
			d = new Dataframe("src/test/resources/emptyFile.csv");
		} catch(Exception e) {}
		assertEquals(0, d.getNbLines());
	}
	
	@Test
	public void testGetCell() {
		try {
			//Test for title
			assertEquals("Mean Streets", d.getCell("Title", 4));
			assertEquals("Jacknife", d.getCell("Title", 23));
			assertEquals("The Score", d.getCell("Title", 53));
			assertEquals("Grudge Match", d.getCell("Title", 80));
			

			//Test for year
			assertEquals(1977, d.getCell("Year", 10));
			assertEquals(1990, d.getCell("Year", 26));
			assertEquals(2008, d.getCell("Year", 67));
			assertEquals(2011, d.getCell("Year", 74));
			

			//Test for score
			assertEquals(7, d.getCell("Score", 79));
			assertEquals(54, d.getCell("Score", 63));
			assertEquals(27, d.getCell("Score", 56));
			assertEquals(41, d.getCell("Score", 52));
		} catch(Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testGetCellWrongLabel() {
		try {
			//Test for wrong label
			d.getCell("Titles", 4);
			fail();
		} catch(Exception e) {}
	}
	
	@Test
	public void testGetCellWrongUnderZeroCell() {
		
		try {
			//Test for wrong label
			d.getCell("Title", -1);
			fail();
		} catch(Exception e) {}
	}
	
	@Test
	public void testGetCellWrongToHightCell() {
		
		try {
			//Test for wrong label
			d.getCell("Title", 88);
			fail();
		} catch(Exception e) {}
	}
	
	
	@Test
	public void testWrongOneTypePerColonne()
	{	
		HashMap<String, ArrayList> wrongDataframe = new HashMap<String, ArrayList>();
		
		ArrayList name = new ArrayList();
		name.add(3.7);		
		name.add("sdfsd");
		name.add(5);		
		
		wrongDataframe.put("nom", name);
		
		ArrayList prenom = new ArrayList();
		prenom.add("Jhon");
		prenom.add("Doe");		
		prenom.add("Thom");		
		wrongDataframe.put("prenom", prenom);
		
		ArrayList genre = new ArrayList();
		prenom.add("F");
		prenom.add("M");		
		prenom.add("M");		
		wrongDataframe.put("genre", genre);
		
		try {
			new Dataframe(wrongDataframe);
			fail();
		} catch(Exception e) {}
	}
	
	@Test
	public void testGoodOneTypePerColonne()
	{	
		HashMap<String, ArrayList> goodDataframe = new HashMap<String, ArrayList>();
		
		ArrayList name = new ArrayList();
		name.add("sdfsd");
		name.add("dsfsd");		
		name.add("sdfdf");		
		
		goodDataframe.put("nom", name);
		
		ArrayList prenom = new ArrayList();
		prenom.add("Jhon");
		prenom.add("Doe");		
		prenom.add("Thom");		
		goodDataframe.put("prenom", prenom);
		
		ArrayList age = new ArrayList();
		age.add(17.3);
		age.add(15.2);		
		age.add(17.2);		
		goodDataframe.put("age", age);
		
		try {
			new Dataframe(goodDataframe);
		} catch(Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testToStringNotNull()
	{
		assertNotEquals("", d.toString());
	}
	
	@Test
	public void testToStringFirstLine()
	{
		assertNotEquals("", d.nFirstLineToString(200));
	}
	
	@Test
	public void testToStringLastLineLessThan0()
	{
		assertNotEquals("", d.nLastLineToString(-54));
	}
	
	@Test
	public void testToStringLastLine()
	{
		assertNotEquals("", d.nLastLineToString(200));
	}
	
	@Test
	public void testSubsetDataframe()
	{
		try {
			Dataframe m = d.subset(0, 15);
			assertEquals(m.toString(), d.nFirstLineToString(15));
		} catch(Exception e) {
			fail(e.getMessage());
		}
		
	}
	
	@Test
	public void testSubsetDataframeLessThanZeroStart()
	{
		try {
			Dataframe m = d.subset(-1, 15);
			fail();
		} catch(Exception e) {}
		
	}
	
	@Test
	public void testSubsetDataframeGreaterThanNbLineStart()
	{
		try {
			Dataframe m = d.subset(180, 15);
			fail();
		} catch(Exception e) {}
		
	}
	
	@Test
	public void testSubsetDataframeGreaterThanEndStart()
	{
		try {
			Dataframe m = d.subset(24, 15);
			fail();
		} catch(Exception e) {}
		
	}
	
	@Test
	public void testSubsetDataframeGreaterThanNbLineEnd()
	{
		try {
			Dataframe m = d.subset(0, 200);
			assertEquals(m.toString(), d.toString());
		} catch(Exception e) {
			fail(e.getMessage());
		}		
	}
	
	@Test
	public void testAverageInteger()
	{
		try {
			assertEquals(58.1954023,d.getAverage("Score"), 0.01);
		}catch(Exception e) {
			fail(e.getMessage());
		}	
	}
	
	@Test
	public void testAverageFloat()
	{
		try {
			assertEquals(0.52,d.getAverage("Prix"), 0.01);
		}catch(Exception e) {
			fail(e.getMessage());
		}	
	}
	
	@Test
	public void testAverageWrongLabel()
	{
		try {
			d.getAverage("Prixs");
			fail();
		}catch(Exception e) {}	
	}
	
	@Test
	public void testAverageNotCalculableRow()
	{
		try {
			d.getAverage("Title");
			fail();
		}catch(Exception e) {}	
	}
	
	@Test
	public void testAverageEmptyData()
	{
		try {
			d = new Dataframe("src/test/resources/noData.csv");
			assertEquals(0,d.getAverage("Prix"),0);
		}catch(Exception e) {
			fail(e.getMessage());
		}	
	}
	
	@Test
	public void testMinFloat()
	{
		try {
			assertEquals(0.1,d.getMin("Prix"),0.00001);
		}catch(Exception e) {
			fail(e.getMessage());
		}	
	}
	
	@Test
	public void testMinInt()
	{
		try {
			assertEquals(4,d.getMin("Score"),0.000001);
		}catch(Exception e) {
			fail(e.getMessage());
		}	
	}
	
	@Test
	public void testMinLabelDoesntExist()
	{
		try {
			d.getMin("Scores");
			fail();
		}catch(Exception e) {}	
	}
	
	@Test
	public void testMinLabelNotCalculable()
	{
		try {
			d.getMin("Title");
			fail();
		}catch(Exception e) {}	
	}
	
	@Test
	public void testMinEmptyData()
	{
		try {
			d = new Dataframe("src/test/resources/noData.csv");
			d.getMin("Score");
			fail();
		}catch(Exception e) {}	
	}
	
	@Test
	public void testMaxFloat()
	{
		try {
			assertEquals(0.91,d.getMax("Prix"),0.00001);
		}catch(Exception e) {
			fail(e.getMessage());
		}	
	}
	
	@Test
	public void testMaxInt()
	{
		try {
			assertEquals(100,d.getMax("Score"),0.000001);
		}catch(Exception e) {
			fail(e.getMessage());
		}	
	}
	
	@Test
	public void testMaxLabelDoesntExist()
	{
		try {
			d.getMax("Scores");
			fail();
		}catch(Exception e) {}	
	}
	
	@Test
	public void testMaxLabelNotCalculable()
	{
		try {
			d.getMax("Title");
			fail();
		}catch(Exception e) {}	
	}
	
	@Test
	public void testMaxEmptyData()
	{
		try {
			d = new Dataframe("src/test/resources/noData.csv");
			d.getMax("Score");
			fail();
		}catch(Exception e) {}	
	}
	
	@Test
	public void testIsCalculableColumn()
	{
		try {
			d.isCalculableColumn("Titles");
			fail();
		}catch(Exception e) {}	
	}
}
