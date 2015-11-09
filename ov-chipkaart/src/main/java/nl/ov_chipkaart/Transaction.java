package nl.ov_chipkaart;

import java.util.Date;

public class Transaction {
	
	private TransactionType type;
	private Date date;
	private Location location;
	
	public Transaction(TransactionType type, Date date, Location location) {
		this.type = type;
		this.date = date;
		this.location = location;
	}

	public TransactionType getType() {
		return type;
	}

	public Date getDate() {
		return date;
	}

	public Location getLocation() {
		return location;
	}
	
}