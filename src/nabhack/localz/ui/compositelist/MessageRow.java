
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
public class MessageRow implements Row {

	private Message message;
	
	/**
	 * Constructor.
	 * 
	 * @param message the message
	 */
    public MessageRow(Message message) {
    		this.message = message;
    }

    /**
     * {@inheritDoc}
     */
    public View getView(int position, View convertView, Context context) {
    	
    	MessageListItem messageListItem;

		if (convertView == null) {
			messageListItem = MessageListItem_.build(context);
		} else {
			messageListItem = (MessageListItem) convertView;
		}
		messageListItem.bind(message);
		return messageListItem;
    }

    /**
     * {@inheritDoc}
     */
    public int getViewType() {
        return RowType.MESSAGE_ROW.ordinal();
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
