package nl.ov_chipkaart;


enum State {
	BALLANCE, TOPUP
}

public class TicketMachineReader  implements Reader {
	
	private State state = State.BALLANCE;
	
	public String onCard(CardData data) {
		
		if (state == State.BALLANCE) {
			state = State.TOPUP;
			
			return String.format("BALLANCE %.2f EUR", data.getCredit()/100.0);
		}
		
		if (state == State.TOPUP) {
			state = State.BALLANCE;

			data.setCredit(data.getCredit() + 1000);
			
			return String.format("NEW BALLANCE %.2f EUR", data.getCredit()/100.0);
		}
		
		throw new IllegalStateException();
	}

}
