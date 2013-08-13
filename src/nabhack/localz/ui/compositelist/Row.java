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
public interface Row {
	
	/**
	 * the row object should start activity and expect a result.
	 */
	String START_ACTIVITY_FOR_RESULT = "start_activity_for_result";
	
	/**
	 * Returns the current view.
	 * 
	 * @param position in the list
	 * @param convertView the view to convert.
	 * @param context the context
	 * @return the current view.
	 */
    View getView(int position, View convertView, Context context);
    
    /**
     * Get the view type.
     * @return the type of view.
     */
    int getViewType();
    
    /**
     * Get intent to for starting activity.
     * 
     * @param context current context.
     * @param position the position of the row in the adapter
   	 * @return intent the new intent.
     */
    Intent getIntent(Context context, int position);


    /**
     * Whether this is a valid row.
     * @return True if valid
     */
    boolean isValid();
}
