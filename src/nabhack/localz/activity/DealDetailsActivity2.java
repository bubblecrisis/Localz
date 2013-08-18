package nabhack.localz.activity;

import nabhack.localz.LocalzApp;
import nabhack.localz.R;
import nabhack.localz.activity.FacebookFragment.IFacebookSessionCallback;
import nabhack.localz.ui.SimpleTextDialog;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;

@OptionsMenu(R.menu.deal_details)
@EActivity(R.layout.activity_deal_details2)
public class DealDetailsActivity2 extends FragmentActivity implements
		IFacebookSessionCallback{

	static final float FADE_DEGREE = 0.35f;

	private static final String TAG = DealDetailsActivity2.class.getSimpleName();

	@App
	LocalzApp application;

	FacebookFragment facebookFragment;

	boolean isLoggingIn;

	boolean isNewFacebookLogin;

	DealDetailsFragment2 dealDetailsFragment;
	
	@ViewById(R.id.claimDeal)
	TextView claimDeal;

	@AfterViews
	void setupView() {
		dealDetailsFragment = new DealDetailsFragment2_();
		facebookFragment = new FacebookFragment_();
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.add(R.id.deal_details_fragment, dealDetailsFragment);
		ft.add(R.id.facebook_fragment, facebookFragment);
		ft.commit();
		dealDetailsFragment.setDeal(application.getCurrentDeal());
		claimDeal.setText("To claim this deal head to "
				+ application.getCurrentDeal().getStore().getName() + " and TAP!");
		initMenuOPtions();
	}

	void initMenuOPtions() {
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getActionBar().setCustomView(R.layout.abs_details_layout);
		Typeface rockbFont = Typeface.createFromAsset(this.getAssets(),
				"rockb.ttf");
		((TextView) getActionBar().getCustomView().findViewById(
				R.id.actionbar_header)).setTypeface(rockbFont);

		ImageView menuIcon = (ImageView) findViewById(R.id.abs_home_menu_id);
		menuIcon.setVisibility(View.GONE);
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2d3e50")));
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		if (item.getItemId() == R.id.action_share) {
			promptUserToPostDetailOnfacebook();
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@UiThread
	public void promptUserToPostDetailOnfacebook() {
		final SimpleTextDialog cancelDialog = new SimpleTextDialog.Builder()
				.body(R.string.msg_facebook_post_on_wall)
				.middleButtonVisible(false)
				.sendButtonText(R.string.facebook_share_to_wall_button_text)
				.cancelButtonText(R.string.common_cancel_button_text)
				.build(DealDetailsActivity2.this);

		cancelDialog.setOnCancelClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				cancelDialog.getDialog().dismiss();
			}
		});

		cancelDialog.setOnSendClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				cancelDialog.getDialog().dismiss();
				if (!facebookFragment.isSessionOpen() && !isLoggingIn) {
					isLoggingIn = true;
					isNewFacebookLogin = true;
					facebookFragment.logoff();
					facebookFragment.loginViaWebDialog();
				} else {
					postDealOnFacebook();
				}
			}
		});
		cancelDialog.show();
	}

	@Override
	public void onFacebookFragmentReady(Session session) {
		// TODO Auto-generated method stub

	}

	public void postDealOnFacebook() {
		Session session = Session.getActiveSession();

		if (session != null) {
			Log.i(TAG, "Posting deal on facebook");
			Bundle postParams = new Bundle();
			postParams.putString("name", "Localz");
			postParams.putString("caption","Finding deals at your local shopping centre.");
			postParams.putString("description", application.getCurrentDeal().getDescription());
			postParams.putString("link","https://www.localz.com.au");
			postParams.putString("picture","http://images.localz.co/localz/localz_fb.png");

			Request.Callback callback = new Request.Callback() {
				public void onCompleted(Response response) {
					JSONObject graphResponse = response.getGraphObject()
							.getInnerJSONObject();
					String postId = null;
					try {
						postId = graphResponse.getString("id");
						showFacebookShareToast();
					} catch (JSONException e) {
						Log.e(TAG, "error:" + e.getMessage());
					}
					FacebookRequestError error = response.getError();
					if (error != null) {
					}
				}
			};

			Request request = new Request(session, "me/feed", postParams,
					HttpMethod.POST, callback);
			RequestAsyncTask task = new RequestAsyncTask(request);
			task.execute();
		}
	}

	@UiThread
	public void showFacebookShareToast() {
		Toast.makeText(DealDetailsActivity2.this, "Deal shared on Facebook",
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onFacebookSessionStateChange(Session session,
			SessionState state, Exception exception) {
		// TODO Auto-generated method stub
		
	}

}
