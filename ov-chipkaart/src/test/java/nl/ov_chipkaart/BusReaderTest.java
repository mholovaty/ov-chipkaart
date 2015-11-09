package nl.ov_chipkaart;

import static org.mockito.Mockito.when;

import java.util.Calendar;

import org.mockito.Mockito;

import junit.framework.TestCase;


public class BusReaderTest extends TestCase {
			
	Calendar cal;
	DateTime dateTime;
	Transport transport;
	TransportMap map;
	BusReader reader;
	TransportRate rate;

	public void setUp() throws Exception {
		cal = Calendar.getInstance();
		cal.set(2000, 1, 1, 12, 0, 0);
		
		dateTime = Mockito.mock(DateTime.class);
		transport = Mockito.mock(Transport.class);
		map = Mockito.mock(TransportMap.class);
		rate = new TransportRate(); 
		
		reader = new BusReader(dateTime, transport, map, rate);
	}

	public void test() {
		CardData data = new CardData();
		data.setCredit(200);
		
		Location from = new Location();
		Location to = new Location();
		
		when(map.getDistance(from, to)).thenReturn(10.0);
		
		when(dateTime.getDate()).thenReturn(cal.getTime());
		when(transport.getLocation()).thenReturn(from);		
		
		assertEquals("OK", reader.onCard(data));
		
		cal.add(Calendar.MINUTE, rate.MAX_TRANSFER_TIME - 10);
		when(dateTime.getDate()).thenReturn(cal.getTime());
		when(transport.getLocation()).thenReturn(to);

		assertEquals("GOOD BYE. You have payed 1.88 EUR, 0.12 EUR left on the card.", reader.onCard(data));
		assertEquals( 12, data.getCredit());

		cal.add(Calendar.MINUTE, 10);
		when(dateTime.getDate()).thenReturn(cal.getTime());
		when(transport.getLocation()).thenReturn(from);		
		
		assertEquals("TRANSFER OK", reader.onCard(data));
		assertEquals( 12, data.getCredit());
		
		when(transport.getLocation()).thenReturn(to);
		
		assertEquals("GOOD BYE. You have payed 1.10 EUR, -0.98 EUR left on the card.", reader.onCard(data));		
		assertEquals(-98, data.getCredit());	

		assertEquals("NOT ENOUGH MONEY", reader.onCard(data));		
		assertEquals(-98, data.getCredit());
	}

}
