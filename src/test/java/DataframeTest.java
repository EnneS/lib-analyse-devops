import static org.junit.Assert.*;

import java.util.ArrayList;
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
		d = new Dataframe("src/test/resources/addresses.csv");
	}

	@After	
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testSize() {
		assertEquals(6, d.getNbLines());
		assertEquals(6, d.getNbColumns());
	}
	
	@Test
	public void testGetCell() {
		assertEquals("John", d.getCell(0, 0));
		assertEquals("Jack", d.getCell(0, 1));
		assertEquals(8075, d.getCell(5, 0));
		assertEquals("John \"Da Man, da\", \", da,\"", d.getCell(0, 2));
		assertEquals("7452 Terrace \"At the Plaza\" road", d.getCell(2, 3));
	}
	
	@Test
	public void testWrongOneTypePerColonne()
	{	
		ArrayList<ArrayList> wrongDataframe = new ArrayList<ArrayList>();
		ArrayList line = new ArrayList<Integer>();
		line.add(0);
		line.add(1);		
		line.add(2);		
		wrongDataframe.add(line);
		
		line = new ArrayList();
		line.add(0);
		line.add(3);		
		line.add(4);		
		wrongDataframe.add(line);
		
		line = new ArrayList();
		line.add("test");
		line.add(5.3);		
		line.add("dssfdsf");		
		wrongDataframe.add(line);
		
		line = new ArrayList();
		line.add(5.3);
		line.add(4.2);		
		line.add(4.5);		
		wrongDataframe.add(line);
		
		try {
			new Dataframe(wrongDataframe);
			fail();
		} catch(Exception e) {}
	}
	
	@Test
	public void testGoodOneTypePerColonne()
	{	
		ArrayList<ArrayList> wrongDataframe = new ArrayList<ArrayList>();
		ArrayList line = new ArrayList<Integer>();
		line.add(0);
		line.add(1);		
		line.add(2);		
		wrongDataframe.add(line);
		
		line = new ArrayList();
		line.add(0);
		line.add(3);		
		line.add(4);		
		wrongDataframe.add(line);
		
		line = new ArrayList();
		line.add("test");
		line.add("5.3");		
		line.add("dssfdsf");		
		wrongDataframe.add(line);
		
		line = new ArrayList();
		line.add(5.3);
		line.add(4.2);	
		line.add(4.5);
		wrongDataframe.add(line);
		
		try {
			new Dataframe(wrongDataframe);
		} catch(Exception e) {
			fail();
		}
	}
}
