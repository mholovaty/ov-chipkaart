package nl.ov_chipkaart;

import static org.junit.Assert.assertEquals;

import org.junit.Before;

public class TicketMachineReaderTest {
	
	TicketMachineReader reader;

	@Before
	public void setUp() {
		reader = new TicketMachineReader();
	}
	
	public void test() {		
		CardData data = new CardData();
		
		assertEquals("BALLANCE 0.00 EUR", reader.onCard(data));
		assertEquals(0, data.getCredit());
		
		assertEquals("NEW BALLANCE 10.00 EUR", reader.onCard(data));
		assertEquals(1000, data.getCredit());
		
		assertEquals("BALLANCE 10.00 EUR", reader.onCard(data));
		assertEquals(1000, data.getCredit());
		
		assertEquals("NEW BALLANCE 20.00 EUR", reader.onCard(data));
		assertEquals(2000, data.getCredit());
	}

}
