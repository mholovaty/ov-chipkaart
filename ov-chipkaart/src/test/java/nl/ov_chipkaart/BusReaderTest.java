package nl.ov_chipkaart;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

public class BusReaderTest {
			
	Calendar cal;
	DateTime dateTime;
	Transport transport;
	TransportMap map;
	BusReader reader;
	TransportRate rate;

	@Before
	public void setUp() {
		cal = Calendar.getInstance();
		cal.set(2000, 1, 1, 12, 0, 0);
		
		dateTime = mock(DateTime.class);
		transport = mock(Transport.class);
		map = mock(TransportMap.class);
		rate = new TransportRate(); 
		
		reader = new BusReader(dateTime, transport, map, rate);
	}
	
	void moveClock(int minutes) {
		cal.add(Calendar.MINUTE, minutes);
		when(dateTime.getDate()).thenReturn(cal.getTime());
	}
	
	void moveLocation(Location location) {
		when(transport.getLocation()).thenReturn(location);
	}
	
	@Test
	public void test() {
		CardData data = new CardData();
		data.setCredit(200);
		
		Location from = new Location();
		Location to = new Location();
		
		when(map.getDistance(from, to)).thenReturn(10.0);
		
		moveClock(0);
		moveLocation(from);
		
		assertEquals("OK", reader.onCard(data));
		
		moveClock(rate.MAX_TRANSFER_TIME - 10);
		moveLocation(to);

		assertEquals("GOOD BYE. You have payed 1.88 EUR, 0.12 EUR left on the card.", reader.onCard(data));
		assertEquals( 12, data.getCredit());

		moveClock(10);
		moveLocation(from);
		
		assertEquals("TRANSFER OK", reader.onCard(data));
		assertEquals( 12, data.getCredit());
		
		moveLocation(to);
		
		assertEquals("GOOD BYE. You have payed 1.10 EUR, -0.98 EUR left on the card.", reader.onCard(data));		
		assertEquals(-98, data.getCredit());	

		assertEquals("NOT ENOUGH MONEY", reader.onCard(data));		
		assertEquals(-98, data.getCredit());
	}

}
