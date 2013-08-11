
package nabhack.localz.ui;


import nabhack.localz.R;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple text dialog that has one title and body of text. The builder defaults to a dialog with
 * no title, and two buttons (OK and Cancel).
 * @author rlay
 */
public final class SimpleTextDialog {
	
	private static final String TAG = SimpleTextDialog.class.getName();

	private final int header;
	private final String headerString;
	private final int body;
	private final String bodyString;
	private final int cancelButtonText;
	private final int middleButtonText;
	private final int sendButtonText;
	private final boolean isScrollable;
	private final boolean middleButtonEnabled;
	private final boolean cancelButtonEnabled;
    private final boolean cancelledOnOutsideTouch;
	private final Context context;
	private final Dialog dialog;
	private Button sendButton;
	private Button cancelButton;
	private Button middleButton;

	
	private SimpleTextDialog(Builder builder, Context context) {
		this.header = builder.header;
		this.headerString = builder.headerString;
		this.bodyString = builder.bodyString;
		this.body = builder.body;
		this.cancelButtonText = builder.cancelButtonText;
		this.middleButtonText = builder.middleButtonText;
		this.sendButtonText = builder.sendButtonText;
		this.middleButtonEnabled = builder.middleButtonVisible;
		this.cancelButtonEnabled = builder.cancelButtonVisible;
		this.isScrollable = builder.scrollable;
        this.cancelledOnOutsideTouch = builder.cancelledOnOutsideTouch;
		this.context = context;
		this.dialog = createDialog();
	}
	
	private Dialog createDialog() {
		final android.app.Dialog d = new android.app.Dialog(context);
		d.requestWindowFeature(Window.FEATURE_NO_TITLE);
		d.setCanceledOnTouchOutside(true);
		
		if (isScrollable) {
			d.setContentView(R.layout.alert_dialog_scrollable_holo);
		} else {
			d.setContentView(R.layout.alert_dialog_holo);
		}
		
		setUpHeader(d);
		setUpBody(d);
		
		middleButton = (Button) d.findViewById(R.id.button3);
		if (!middleButtonEnabled) {
			d.findViewById(R.id.button3).setVisibility(View.GONE);
		} else {
			middleButton.setText(middleButtonText);
		}
		
		sendButton = (Button) d.findViewById(R.id.button1);
		if (sendButtonText > 0) {
			sendButton.setText(sendButtonText);
		} else {
			sendButton.setText(R.string.common_OK_button_text);
		}
		
		cancelButton = (Button) d.findViewById(R.id.button2);
		if (cancelButtonText > 0) {
			cancelButton.setText(cancelButtonText);
		} else {
			cancelButton.setText(R.string.common_cancel_button_text);
		}
		
		if (!cancelButtonEnabled) {
			cancelButton.setVisibility(View.GONE);
		}
		
		setOnCancelClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				d.dismiss();
			}
		});
		setOnSendClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				d.dismiss();
			}
		});
		
		return d;
	}

	private void setUpHeader(final Dialog d) {
		if (header > 0) {
			((TextView) d.findViewById(R.id.dialog_header)).setText(header);
		} else if (!TextUtils.isEmpty(headerString)) {
			((TextView) d.findViewById(R.id.dialog_header)).setText(headerString);
		} else {
			d.findViewById(R.id.dialog_header).setVisibility(View.GONE);
			d.findViewById(R.id.buttonPanelTopDivider).setVisibility(View.GONE);
		}
	}

	private void setUpBody(final Dialog d) {
		if (body > 0) {
			TextView bodyTextView = (TextView) d.findViewById(R.id.dialog_text);
			bodyTextView.setText(body);
			bodyTextView.setVisibility(View.VISIBLE);
		} else if (!TextUtils.isEmpty(bodyString)) {
			TextView bodyTextView = (TextView) d.findViewById(R.id.dialog_text);
			bodyTextView.setText(bodyString);
			bodyTextView.setVisibility(View.VISIBLE);
		}
	}
	
	public Dialog getDialog() {
		return dialog;
	}
	
	/**
	 * Shows the dialog on screen.
     * @return This simple text dialog
	 */
	public SimpleTextDialog show() {
        if (cancelledOnOutsideTouch) {
            dialog.setCanceledOnTouchOutside(true);
        }
		dialog.setCancelable(false);
		dialog.show();
		return this;
	}
	
	/**
	 * Dismisses the dialog on screen.
	 */
	public void dismiss() {
		dialog.dismiss();
	}
	
	/**
	 * Sets listener for on cancel event.
	 * @param listener The listener
	 */
	public void setOnCancelClickListener(OnClickListener listener) {
		cancelButton.setOnClickListener(listener);
	}
	
	/**
	 * Sets listener for on send event.
	 * @param listener The listener
	 */
	public void setOnSendClickListener(OnClickListener listener) {
		sendButton.setOnClickListener(listener);
	}
	
	/**
	 * Set listener for the middle button.
	 * @param listener The listener
	 */
	public void setOnMiddleClickListener(OnClickListener listener) {
		middleButton.setOnClickListener(listener);
	}

	
	/**
	 * Set listener on key pressed on dialog.
	 * 
	 * @param listener The listener 
	 */	
	public void setOnKeyListener(DialogInterface.OnKeyListener listener) {
		dialog.setOnKeyListener(listener);
	}
	
    /**
     * Shows a dialog with a message and OK button only. Must be called from UI thread.
     * @param string The string to show.
     * @param context The application context
     * @return The simple text dialog object
     */
    public static SimpleTextDialog popup(String string, Context context) {
        return new Builder()
                .body(string)
                .cancelButtonVisible(false)
                .build(context)
                .show();
    }

    /**
     * Shows a dialog with a message and OK button only. Must be called from UI thread.
     * @param stringId The resource id of the string to show.
     * @param context The application context
     * @return The simple text dialog object
     */
    public static SimpleTextDialog popup(int stringId, Context context) {
        return new Builder()
                .body(stringId)
                .cancelButtonVisible(false)
                .build(context)
                .show();
    }

	/**
	 * Builder class for simple text dialogs. The builder defaults to a dialog with
     * no title, and two buttons (OK and Cancel).
	 * @author rlay
	 */
	public static class Builder {
		
		private int header;
		private int body;
		private int cancelButtonText;
		private int middleButtonText;
		private int sendButtonText;
		private boolean middleButtonVisible;
		private boolean scrollable = true;
		private String headerString;
		private String bodyString;
		private boolean cancelButtonVisible = true;
        private boolean cancelledOnOutsideTouch;
		
		/**
		 * Sets the heading of the dialog.
		 * @param stringId String resource id of the heading
		 * @return The builder object
		 */
		public Builder header(int stringId) {
			header = stringId;
			return this;
		}
		
		/**
		 * Sets the heading of the dialog.
		 * @param headerStr Heading as a string
		 * @return The builder object
		 */
		public Builder header(String headerStr) {
			this.headerString = headerStr;
			return this;
		}
		
		/**
		 * Sets the top text body.
		 * @param stringId String resource id of the body
		 * @return The builder object
		 */
		public Builder body(int stringId) {
			body = stringId;
			return this;
		}
		
		/**
		 * Sets the top text body.
		 * @param string The body as a string
		 * @return The builder object
		 */
		public Builder body(String string) {
			bodyString = string;
			return this;
		}

		/**
		 * Sets cancel button text.
		 * @param stringId String resource id of the text
		 * @return The builder object
		 */
		public Builder cancelButtonText(int stringId) {
			cancelButtonText = stringId;
			return this;
		}
		
		/**
		 * Sets send button text.
		 * @param stringId String resource id of the text
		 * @return The builder object
		 */
		public Builder sendButtonText(int stringId) {
			sendButtonText = stringId;
			return this;
		}
		
		/**
		 * Sets middle button text.
		 * @param stringId String resource id of the text
		 * @return The builder object
		 */
		public Builder middleButtonText(int stringId) {
			middleButtonText = stringId;
			return this;
		}
		
		/**
		 * Sets whether the middle button is visible.
		 * @param visible True if visible
		 * @return The builder object
		 */
		public Builder middleButtonVisible(boolean visible) {
			middleButtonVisible = visible;
			return this;
		}
		
		/**
		 * Sets whether the cancel button is visible.
		 * @param visible True if visible
		 * @return The builder object
		 */
		public Builder cancelButtonVisible(boolean visible) {
			cancelButtonVisible = visible;
			return this;
		}

        /**
         * Sets whether the dialog should be cancelled when touched outside.
         * @param cancel True if should be cancelled
         * @return The builder object
         */
        public Builder cancelOnTouchOutside(boolean cancel) {
            cancelledOnOutsideTouch = cancel;
            return this;
        }

		/**
		 * Builds the PaymentDialog object.
		 * @param context The context of the dialog
		 * @return The PaymentDialog object
		 */
		public SimpleTextDialog build(Context context) {
			return new SimpleTextDialog(this, context); 
		}

		/**
		 * Sets whether the dialog is scrollable.
		 * @param isScrollable whether dialog should scroll.
		 * @return The builder object
		 */
		public Builder scrollable(boolean isScrollable) {
			scrollable = isScrollable;
			return this;
		}
	}
}
