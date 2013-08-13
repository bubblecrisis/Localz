
package nabhack.localz.ui.compositelist;

/**
 * Contains all the data used to display a menu item in a composite list. 
 * The menu item is used to display links to other activities.
 * 
 * @author markng
 */
public class Menu {

	private int name;
	
	private int icon;
	
	private MenuType type;
	
	private boolean displayDivider;

	/**
	 * The menu type.
	 * 
	 * @author markng
	 */
	public enum MenuType {
		SETTINGS,
		CONTACT_NAB,
		LOGOUT;	
	}
	
	/**
	 * Constructor.
	 * 
	 * @param name id of the string to display
	 * @param icon id of the image to display
	 * @param type the type of menu
	 */
	public Menu(int name, int icon, MenuType type) {
		this.name = name;
		this.icon = icon;
		this.type = type;
		this.displayDivider = true;
	}
	
	/**
	 * Constructor.
	 * 
	 * @param name id of the string to display
	 * @param icon id of the image to display
	 * @param type the type of menu
	 * @param displayDivider flag used to display/hide divider
	 */
	public Menu(int name, int icon, MenuType type, boolean displayDivider) {
		this.name = name;
		this.icon = icon;
		this.type = type;
		this.displayDivider = displayDivider;
	}
	
	
	public int getName() {
		return name;
	}

	public void setName(int name) {
		this.name = name;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}


	public MenuType getType() {
		return type;
	}


	public void setType(MenuType type) {
		this.type = type;
	}

	public boolean isDisplayDivider() {
		return displayDivider;
	}

	public void setDisplayDivider(boolean displayDivider) {
		this.displayDivider = displayDivider;
	}
}
