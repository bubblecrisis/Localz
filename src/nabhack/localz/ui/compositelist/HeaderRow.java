package nabhack.localz.ui.compositelist;

import android.content.Context;
import android.content.Intent;
import android.view.View;

/**
 * Represents a header that can be displayed in the list view.
 * 
 * @author Mark Ng
 *
 */
public class HeaderRow implements Row {

	private Header header;
	
	/**
	 * Constructor.
	 * 
	 * @param header the header
	 */
    public HeaderRow(Header header) {
    		this.header = header;
    }

    /**
     * {@inheritDoc}
     */
    public View getView(int position, View convertView, Context context) {
    	
    	HeaderListItem headerListItem;

		if (convertView == null) {
			headerListItem = HeaderListItem_.build(context);
		} else {
			headerListItem = (HeaderListItem) convertView;
		}
		headerListItem.bind(header);
		return headerListItem;
    }

    /**
     * {@inheritDoc}
     */
    public int getViewType() {
        return RowType.HEADER_ROW.ordinal();
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
