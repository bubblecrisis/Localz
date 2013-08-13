
package nabhack.localz.ui.compositelist;

/**
 * Contains all the data used to display a message item in a composite list. 
 * 
 * @author markng
 */
public class Message {

	private int name;
			
	/**
	 * Constructor.
	 * 
	 * @param name id of the string to display
	 */
	public Message(int name) {
		this.name = name;
	}
		
	public int getName() {
		return name;
	}

	public void setName(int name) {
		this.name = name;
	}
}
