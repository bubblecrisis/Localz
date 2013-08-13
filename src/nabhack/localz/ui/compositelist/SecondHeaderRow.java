
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
public class SecondHeaderRow implements Row {

	private SecondHeader secondHeader;
	
	/**
	 * Constructor.
	 * 
	 * @param secondHeader the secondHeader
	 */
    public SecondHeaderRow(SecondHeader secondHeader) {
    		this.secondHeader = secondHeader;
    }

    /**
     * {@inheritDoc}
     */
    public View getView(int position, View convertView, Context context) {
    	
    	SecondHeaderListItem secondHeaderListItem;

		if (convertView == null) {
			secondHeaderListItem = SecondHeaderListItem_.build(context);
		} else {
			secondHeaderListItem = (SecondHeaderListItem) convertView;
		}
		secondHeaderListItem.bind(secondHeader);
		return secondHeaderListItem;
    }

    /**
     * {@inheritDoc}
     */
    public int getViewType() {
        return RowType.SECOND_HEADER_ROW.ordinal();
    }
    
    @Override
	public Intent getIntent(Context context, int position) {
    	return null;
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
