package nabhack.localz.ui.compositelist;

import android.content.Context;
import android.content.Intent;
import android.view.View;

/**
 * Interface to get view from row in list adapter.
 * 
 * @author Mark Ng
 * 
 */
public class MenuRow implements Row {

	private Menu menu;

	/**
	 * Constructor.
	 * 
	 * @param menu
	 *            the menu item.
	 */
	public MenuRow(Menu menu) {
		this.menu = menu;
	}

	/**
	 * {@inheritDoc}
	 */
	public View getView(int position, View convertView, Context context) {
		MenuListItem menuListItem;

		if (convertView == null) {
			menuListItem = MenuListItem_.build(context);
		} else {
			menuListItem = (MenuListItem) convertView;
		}
		menuListItem.bind(menu);
		return menuListItem;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getViewType() {
		return RowType.MENU_ROW.ordinal();
	}

	@Override
	public Intent getIntent(Context context, int position) {
		Intent intent = null;

		switch (menu.getType()) {
			case SETTINGS:
				break;
			case CONTACT_NAB:
				break;
			case LOGOUT:
				break;
			default:
				break;
			}
		return intent;
	}

    @Override
    public boolean isValid() {
        return true;
    }
}
