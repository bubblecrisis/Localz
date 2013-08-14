package nabhack.localz.activity;

import java.io.IOException;
import java.sql.Timestamp;

import nabhack.localz.LocalzApp;
import nabhack.localz.R;
import nabhack.localz.adapter.DealAdapter;
import nabhack.localz.models.Deal;
import nabhack.localz.utils.GCMServerUtilities;
import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ItemClick;
import com.googlecode.androidannotations.annotations.ViewById;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

@EActivity(R.layout.activity_deal_summary)
public class DealSummaryActivity extends FragmentActivity {

	static final float FADE_DEGREE = 0.35f;

	private static final String TAG = "DealSummaryActivity";
	@ViewById
	ListView listView;

	@App
	LocalzApp application;

	SlidingMenu menu;

	SideMenuListFragment sideMenuFragment;

	GoogleCloudMessaging gcmServer;
	String gcmRegid;
	BroadcastReceiver notificationReceiver;

	/**
	 * GCM Constants Start
	 */
	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	private static final String PROPERTY_ON_SERVER_EXPIRATION_TIME = "onServerExpirationTimeMs";
	/**
	 * Default lifespan (7 days) of a reservation until it is considered
	 * expired.
	 */
	public static final long REGISTRATION_EXPIRY_TIME_MS = 1000 * 3600 * 24 * 7;
	/**
	 * This is project id from Google API Console
	 */
	String SENDER_ID = "26193842183";

	/**
	 * GCM Constants End
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		gcmRegid = getRegistrationId(getApplicationContext());

		if (gcmRegid.length() == 0) {
			registerBackground();
		}
		gcmServer = GoogleCloudMessaging.getInstance(this);
		super.onCreate(savedInstanceState);
	}

	@AfterViews
	void setupView() {
		listView.setAdapter(new DealAdapter(this, R.layout.deal_list_item,
				R.id.title, application.getDealsOnOffer()));
		initSideMenu();
		initMenuOPtions();
	}

	private void initSideMenu() {
		// configure the SlidingMenu
		menu = new SlidingMenu(this);
		menu.setMode(SlidingMenu.LEFT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		menu.setShadowWidthRes(R.dimen.side_menu_shadow_width);
		menu.setShadowDrawable(R.drawable.sidemenu_shadow);
		menu.setBehindOffsetRes(R.dimen.side_menu_offset);
		menu.setFadeDegree(FADE_DEGREE);
		menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
		menu.setMenu(R.layout.menu_frame);
		menu.setOnClosedListener(new SlidingMenu.OnClosedListener() {
			@Override
			public void onClosed() {

			}
		});
		sideMenuFragment = new SideMenuListFragment_();

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, sideMenuFragment).commit();
		menu.setOnOpenListener(new SlidingMenu.OnOpenListener() {
			@Override
			public void onOpen() {
				sideMenuFragment.refresh();
			}
		});

	}

	@ItemClick(R.id.listView)
	void listViewClicked(int position) {
		application.setCurrentDeal((Deal) listView.getItemAtPosition(position));
		Intent intent = new Intent(this, DealDetailsActivity_.class);
		startActivity(intent);
	}

	void initMenuOPtions() {
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getActionBar().setCustomView(R.layout.abs_home_layout);
		ImageView menuIcon = (ImageView) findViewById(R.id.abs_home_menu_id);
		menuIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				menu.toggle();
			}
		});
	}

	/**
	 * Stores the registration id, app versionCode, and expiration time in the
	 * application's {@code SharedPreferences}.
	 * 
	 * @param context
	 *            application's context.
	 * @param regId
	 *            registration id
	 */
	private void setGCMRegistrationId(Context context, String regId) {
		final SharedPreferences prefs = getGCMPreferences(context);
		Log.v(TAG, "Saving GCM regId " + regId + " to shared preferences");
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regId);
		long expirationTime = System.currentTimeMillis()
				+ REGISTRATION_EXPIRY_TIME_MS;

		Log.v(TAG, "Setting registration expiry time to "
				+ new Timestamp(expirationTime));
		editor.putLong(PROPERTY_ON_SERVER_EXPIRATION_TIME, expirationTime);
		editor.commit();
	}

	/**
	 * Gets the current registration id for application on GCM service.
	 * <p>
	 * If result is empty, the registration has failed.
	 * 
	 * @return registration id, or empty string if the registration is not
	 *         complete.
	 */
	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getGCMPreferences(context);
		String registrationId = prefs.getString(PROPERTY_REG_ID, "");
		if (registrationId.length() == 0) {
			Log.v(TAG, "Registration not found.");
			return "";
		}
		// check if app was updated; if so, it must clear registration id to
		// avoid a race condition if GCM sends a message
		int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION,
				Integer.MIN_VALUE);
		if (isRegistrationExpired()) {
			Log.v(TAG, "GCM registration expired.");
			return "";
		}
		return registrationId;
	}

	/**
	 * Registers the application with GCM servers asynchronously.
	 * <p>
	 * Stores the registration id, app versionCode, and expiration time in the
	 * application's shared preferences.
	 */
	private void registerBackground() {
		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {
				String msg = "";
				try {
					if (gcmServer == null) {
						gcmServer = GoogleCloudMessaging
								.getInstance(getApplicationContext());
					}
					gcmRegid = gcmServer.register(SENDER_ID);

					// Sending gcmRegid to Localz server
					// This needs to be changed to real Localz server
					// in Heroku

					GCMServerUtilities.register(getApplicationContext(),
							gcmRegid);

					// Save the regid to SharedPreferences - no need to register
					// again.
					setGCMRegistrationId(getApplicationContext(), gcmRegid);
				} catch (IOException ex) {
					msg = "Error :" + ex.getMessage();
				}
				return msg;
			}

			@Override
			protected void onPostExecute(String msg) {
				Log.d(TAG, "Device registered, registration id=" + gcmRegid);
			}
		}.execute(null, null, null);
	}

	/**
	 * @return Application's {@code SharedPreferences}.
	 */
	private SharedPreferences getGCMPreferences(Context context) {
		return getSharedPreferences("LOCALZ", Context.MODE_PRIVATE);
	}

	/**
	 * Checks if the registration has expired.
	 * 
	 * <p>
	 * To avoid the scenario where the device sends the registration to the
	 * server but the server loses it, the app developer may choose to
	 * re-register after REGISTRATION_EXPIRY_TIME_MS.
	 * 
	 * @return true if the registration has expired.
	 */
	private boolean isRegistrationExpired() {
		final SharedPreferences prefs = getGCMPreferences(getApplicationContext());
		// checks if the information is not stale
		long expirationTime = prefs.getLong(PROPERTY_ON_SERVER_EXPIRATION_TIME,
				-1);
		return System.currentTimeMillis() > expirationTime;
	}

	private class NotificationBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// Here need to pull new deal, etc. leave it to Chris
			Toast.makeText(context, intent.getExtras().getString("Message"),
					Toast.LENGTH_LONG).show();
		}
	}
}
