package nl.ov_chipkaart;

import java.util.Calendar;
import java.util.Date;

public class BusReader implements Reader {
	
	private Calendar cal;
	private DateTime dateTime;
	private Transport transport;
	private TransportMap map;
	private TransportRate rate;
	
	public BusReader(
			DateTime dateTime,
			Transport transport,
			TransportMap map,
			TransportRate rate) {
		this.cal = Calendar.getInstance();
		this.dateTime = dateTime;
		this.transport = transport;
		this.map = map;
		this.rate = rate;
	}
	
	public String onCard(CardData data) {
		
		if (data.isCheckedOut())
			return checkIn(data);
		
		if (data.isCheckedIn())
			return checkOut(data);
		
		throw new IllegalStateException();
	}	

	private String checkIn(CardData data) {
		int credit = data.getCredit();

		if (credit <= 0)
			return "NOT ENOUGH MONEY";
		
		Date now = dateTime.getDate();
		
		// Check is transfer
		cal.setTime(now);
		cal.add(Calendar.MINUTE, -rate.MAX_TRANSFER_TIME);
		boolean isTransfer = data.isTransfer(cal.getTime()); 
		
		Location from = transport.getLocation();
		
		// TODO: Take deposit
		
		data.setCredit(credit);
		
		if (isTransfer) {
			data.setTransaction(new Transaction(TransactionType.TRANSFER, now, from));
			return "TRANSFER OK";
		}
		else {
			data.setTransaction(new Transaction(TransactionType.IN, now, from));
			return "OK";
		}
	}
	
	private String checkOut(CardData data) {
		Transaction transaction = data.getTransaction();

		Location from = transaction.getLocation();
		Location to = transport.getLocation();
		
		double distance = map.getDistance(from, to);

		int credit = data.getCredit();
		
		// TODO: Return deposit
		
		int pay = (int) Math.round(rate.KILOMETER_RATE * distance);
		
		if (transaction.getType() != TransactionType.TRANSFER)
			pay += rate.BASIC_RATE;

		credit -= pay;
		
		data.setCredit(credit);
		data.setTransaction(new Transaction(TransactionType.OUT, dateTime.getDate(), to));
		
		return String.format("GOOD BYE. You have payed %.2f EUR, %.2f EUR left on the card.", pay/100.0, credit/100.0);		
	}


}
