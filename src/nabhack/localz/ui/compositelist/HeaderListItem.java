
package nabhack.localz.ui.compositelist;

import nabhack.localz.R;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.ViewById;

/**
 *  View used to display the menu in a composite list.
 * 
 * @author markng
 * 
 */
@EViewGroup(R.layout.composite_list_header)
public class HeaderListItem extends LinearLayout {

	public static final String TAG = "HeaderListItem";
	
	@ViewById(R.id.header)
	TextView text;

	Context context;
	
	/**
	 * Constructor.
	 * 
	 * @param context
	 *            Context
	 */
	public HeaderListItem(Context context) {
		super(context);
		this.context = context;
	}

	/**
	 * Bind data.
	 * @param header data to bind.
	 */
	public void bind(Header header) {
		text.setText(header.getName());
	}
}
