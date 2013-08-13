package nabhack.localz.ui.compositelist;

/**
 * Contains all the data used to display a header item in a composite list. 
 * 
 * @author markng
 */
public class SecondHeader {

	private String name;
			
	/**
	 * Constructor.
	 * 
	 * @param name id of the string to display
	 */
	public SecondHeader(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
		

}
