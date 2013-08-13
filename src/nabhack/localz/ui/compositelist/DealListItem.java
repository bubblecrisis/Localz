

package nabhack.localz.ui.compositelist;

import nabhack.localz.R;
import nabhack.localz.models.Deal;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.ViewById;

/**
 * View used to display the token transaction in the side menu.
 * 
 * @author markng
 * 
 */
@SuppressLint("DefaultLocale")
@EViewGroup(R.layout.side_menu_deal_list_item)
public class DealListItem extends RelativeLayout {

	static final String TAG = "TokenTxnListItem";

	static final String TYPE_TEXT_KEY = "token_txn_{type}_name";

	Context context;

	@ViewById(R.id.side_menu_payment_deal_summary_text)
	TextView dealSummary;

	@ViewById(R.id.side_menu_payment_deal_date_text)
	TextView dealDate;

	@ViewById(R.id.side_memu_payment_status_divider)
	View divider;
	
	/**
	 * Constructor.
	 * 
	 * @param context
	 *            Context
	 */
	public DealListItem(Context context) {
		super(context);
		this.context = context;
	}

	/**
	 * Bind data.
	 * 
	 * @param token
	 *            data to bind.
	 * @param style
	 *            style to display the token data.
	 * @param displayDivider
	 *            display the divider
	 */
	public void bind(Deal deal, boolean displayDivider) {
		dealSummary.setText(deal.getTitle());
		//todo set date.
		
		if (displayDivider) {
			divider.setVisibility(View.VISIBLE);
		} else {
			divider.setVisibility(View.GONE);
		}
	}

}
