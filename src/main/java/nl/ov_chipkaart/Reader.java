package nl.ov_chipkaart;

/**
 * OV-chipkaart reader logic.
 * <p>
 * The reader works in a following cycle
 * <ul>
 * <li>read card data
 * <li>process card data
 * <li>save card data
 * <li>display result message
 * </ul>
 *
 */
public interface Reader {
	
	public String onCard(CardData data);

}
