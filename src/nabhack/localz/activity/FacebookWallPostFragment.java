
package nabhack.localz.activity;

import nabhack.localz.R;
import nabhack.localz.ui.SimpleTextDialog;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.res.StringRes;

/**
 * Fragment that manages Facebook wall posts. This fragment is intended to be an invisible
 * fragment that runs in the background.
 * 
 * @author rlay
 */

@EFragment
public class FacebookWallPostFragment extends Fragment {

	private static final String TAG = FacebookWallPostFragment.class.getSimpleName();
	
	@StringRes(R.string.facebook_share_to_wall_name)
	protected String facebookName;

	@StringRes(R.string.facebook_share_to_wall_caption)
	protected String facebookCaption;

	@StringRes(R.string.facebook_share_to_wall_description)
	protected String facebookDesc;

	@StringRes(R.string.facebook_share_to_wall_description)
	protected String facebookRequestMoneyDesc;
	
	@StringRes(R.string.facebook_share_to_wall_link)
	protected String facebookLink;
	
	@StringRes(R.string.facebook_share_to_wall_picture)
	protected String facebookPicture;

	private boolean isResumed;

	private IFacebookWallPostCallback facebookCallback;

	private boolean cancelAlreadyPressed;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
    }
    
	/**
	 * Callback to parent activity.
	 */
	public interface IFacebookWallPostCallback {

		/**
		 * Called when the wall post was specifically cancelled by the user.
		 */
		void onFacebookWallPostCancelled();

		/**
		 * Called when the wall post was successfully posted.
		 */
		void onFacebookWallPostCompleted();
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof IFacebookWallPostCallback) {
			facebookCallback = (IFacebookWallPostCallback) activity;
		} else {
			throw new ClassCastException(activity.toString() + " must implement " 
					+ FacebookWallPostFragment.class.getName() + "." + IFacebookWallPostCallback.class.getName());
		}
	}
	
    @Override
    public void onResume() {
        super.onResume();
        isResumed = true;
    }
    
	/**
	 * Publish a post to another Facebook user's feed.
	 * @param facebookId The facebook ID of the target user to post to
	 */
    @UiThread
	public void publishFeedDialog(final String facebookId) {
		if (isResumed) {
			Bundle params = getFacebookBundle(facebookId);
			WebDialog feedDialog = new WebDialog.FeedDialogBuilder(getActivity(), 
				Session.getActiveSession(),
				params).setOnCompleteListener(new OnCompleteListener() {
					
					@Override
					public void onComplete(Bundle values, FacebookException error) {
						
						// Cancel prompt user to post again.
						if ((values != null && values.isEmpty() && error == null) 
								|| error instanceof FacebookOperationCanceledException) {
							if (cancelAlreadyPressed) {
								facebookCallback.onFacebookWallPostCancelled();
							} else {
								cancelAlreadyPressed = true;
								cancelFacebookDialog(facebookId);
							}
							return;
						}
						// Error prompt user to retry
						if (error != null && !(error instanceof FacebookOperationCanceledException)) {
							errorFacebookDialog(facebookId);
							return;
						}
						
						facebookCallback.onFacebookWallPostCompleted();
					}
				}).build();
			feedDialog.show();
		}
	}

	private void cancelFacebookDialog(final String facebookId) {
		final SimpleTextDialog cancelDialog = new SimpleTextDialog.Builder()
				.body(R.string.msg_facebook_post_on_wall)
				.middleButtonVisible(false)
				.sendButtonText(R.string.facebook_share_to_wall_button_text)
				.cancelButtonText(R.string.common_cancel_button_text)
				.build(getActivity());

		cancelDialog.setOnCancelClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				cancelDialog.getDialog().dismiss();
				facebookCallback.onFacebookWallPostCancelled();
			}
		});

		cancelDialog.setOnSendClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				cancelDialog.getDialog().dismiss();
				publishFeedDialog(facebookId);
			}
		});
		cancelDialog.show();
	}

	private void errorFacebookDialog(final String facebookId) {
		final SimpleTextDialog retryDialog = new SimpleTextDialog.Builder()
				.body(R.string.msg_error_failed_to_post_to_facebook_wall)
				.middleButtonVisible(false)
				.sendButtonText(R.string.facebook_share_to_wall_button_text)
				.cancelButtonText(R.string.common_cancel_button_text)
				.build(getActivity());

		retryDialog.setOnCancelClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				retryDialog.getDialog().dismiss();
				facebookCallback.onFacebookWallPostCancelled();
			}
		});

		retryDialog.setOnSendClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				retryDialog.getDialog().dismiss();
				publishFeedDialog(facebookId);
			}
		});
		retryDialog.show();
	}
	
	private Bundle getFacebookBundle(String facebookId) {
	    Bundle params = new Bundle();
	    params.putString("name", facebookName);
	    params.putString("caption", facebookCaption);
	    params.putString("to", facebookId);
	    params.putString("link", facebookLink);
	    params.putString("picture", facebookPicture);
	    params.putString("description", facebookDesc);	    	
	    return params; 
	}
}
