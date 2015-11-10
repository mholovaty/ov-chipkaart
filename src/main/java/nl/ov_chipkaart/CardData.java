package nl.ov_chipkaart;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * OV-chipkaart stored data.
 * 
 * <ul>
 * 	<li>credit
 * 	<li>last 10 travel transactions
 * </ul>
 */
public class CardData {
	
	private int credit = 0;
	
	private LinkedList<Transaction> transactions = new LinkedList<Transaction>();

	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}
	
	public List<Transaction> getTransactions() {
		return Collections.unmodifiableList(transactions);
	}
	
	public Transaction getTransaction() {
		return transactions.getFirst();
	}
	
	public void setTransaction(Transaction transaction) {
		if (transactions.size() == 10) {
			transactions.removeLast();
		}
		
		transactions.addFirst(transaction);
	}
	
	public boolean isCheckedIn() {
		if (transactions.isEmpty())
			return false;
		
		if (getTransaction().getType() == TransactionType.IN)
			return true;

		if (getTransaction().getType() == TransactionType.TRANSFER)
			return true;
		
		return false;
	}
	
	public boolean isCheckedOut() {
		return transactions.isEmpty() ||
				getTransaction().getType() == TransactionType.OUT;
	}
	
	public boolean isTransfer(Date max_check_in) {		
		for (Transaction transaction : transactions ) {

			if (transaction.getDate().compareTo(max_check_in) < 0)
				break;
			
			if (transaction.getType() == TransactionType.IN)
				return true;
			
		}		
		return false;
	}

}