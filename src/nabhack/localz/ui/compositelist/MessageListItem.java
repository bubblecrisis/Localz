/*
 * (C) 2012 National Australia Bank
 *
 * [All rights reserved]. This product and related documentation are protected
 * by copyright restricting its use, copying, distribution, and decompilation.
 * No part of this product or related documentation may be reproduced in any
 * form by any means without prior written authorization of
 * National Australia Bank. Unless otherwise arranged, third parties may not
 * have access to this product or related documents.
 */

package nabhack.localz.ui.compositelist;

import nabhack.localz.R;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.EViewGroup;
import com.googlecode.androidannotations.annotations.ViewById;

/**
 *  View used to display a message in a composite list.
 * 
 * @author markng
 * 
 */
@EViewGroup(R.layout.composite_list_message)
public class MessageListItem extends LinearLayout {

	public static final String TAG = "MessageListItem";
	
	@ViewById(R.id.message)
	TextView text;

	Context context;
	
	/**
	 * Constructor.
	 * 
	 * @param context
	 *            Context
	 */
	public MessageListItem(Context context) {
		super(context);
		this.context = context;
	}

	/**
	 * Bind data.
	 * @param message data to bind.
	 */
	public void bind(Message message) {
		text.setText(message.getName());
	}
}
