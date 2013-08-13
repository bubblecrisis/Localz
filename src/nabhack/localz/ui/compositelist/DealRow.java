package nabhack.localz.ui.compositelist;

import nabhack.localz.models.Deal;
import android.content.Context;
import android.content.Intent;
import android.view.View;

/**
 * Interface to get view from row in list adapter.
 * 
 * @author Mark Ng
 * 
 */
public class DealRow implements Row {

	private Deal deal;

	private boolean displayDivider;

    private boolean isValid;

	/**
	 * Constructor.
	 * 
	 * @param token
	 *            the token.
	 * @param style
	 *            display style
	 * @param displayDivider used to determine whether we display a divider
	 */
	public DealRow(Deal deal, boolean displayDivider) {
		this.deal = deal;
		this.displayDivider = displayDivider;
	}

	/**
	 * {@inheritDoc}
	 */
	public View getView(int position, View convertView, Context context) {
		DealListItem dealListItem;
		if (convertView == null) {
			dealListItem = DealListItem_.build(context);
		} else {
			dealListItem = (DealListItem) convertView;
		}
		dealListItem.bind(deal, displayDivider);
		return dealListItem;
	}

	/**
	 * {@inheritDoc}
	 */
	public int getViewType() {
		return RowType.DEAL_ROW.ordinal();
	}

	@Override
	public Intent getIntent(Context context, int position) {
		return null;
	}

    @Override
    public boolean isValid() {
        return isValid;
    }

    public boolean isDisplayDivider() {
		return displayDivider;
	}

	public void setDisplayDivider(boolean displayDivider) {
		this.displayDivider = displayDivider;
	}
}