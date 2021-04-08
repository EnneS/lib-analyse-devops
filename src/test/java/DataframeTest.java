import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
		assertEquals("Jack", d.getCell(1, 0));
		assertEquals("8075", d.getCell(0, 5));
		assertEquals("John \"Da Man, da\", \", da,\"", d.getCell(2, 0));
		assertEquals("7452 Terrace \"At the Plaza\" road", d.getCell(3, 2));
	}

}
