package nabhack.localz.ui.compositelist;

import nabhack.localz.R;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.ViewById;

/**
 * View used to display the menu in the composite list.
 * 
 * @author markng
 * 
 */
@EViewGroup(R.layout.composite_list_link)
public class MenuListItem extends LinearLayout {

	public static final String TAG = "MenuListItem";

	@ViewById(R.id.composite_list_link_image)
	ImageView image;

	@ViewById(R.id.composite_list_link_name)
	TextView text;

	@ViewById(R.id.side_memu_link_divider)
	View divider;

	Context context;

	/**
	 * Constructor.
	 * 
	 * @param context
	 *            Context
	 */
	public MenuListItem(Context context) {
		super(context);
		this.context = context;
	}

	/**
	 * Bind data.
	 * 
	 * @param menu
	 *            data to bind.
	 */
	public void bind(Menu menu) {
		Drawable icon = this.context.getResources().getDrawable(menu.getIcon());
		image.setImageDrawable(icon);
		String name = this.context.getResources().getString(menu.getName());
		text.setText(name);
		if (menu.isDisplayDivider()) {
			divider.setVisibility(View.VISIBLE);
		} else {
			Log.d(TAG, "hiding divider");
			divider.setVisibility(View.GONE);
		}
	}
}
