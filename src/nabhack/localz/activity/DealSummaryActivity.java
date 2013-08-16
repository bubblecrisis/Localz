package nabhack.localz.activity;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import nabhack.localz.LocalzApp;
import nabhack.localz.R;
import nabhack.localz.adapter.DealAdapter;
import nabhack.localz.models.BasicResponse;
import nabhack.localz.models.Category;
import nabhack.localz.models.Deal;
import nabhack.localz.models.DeviceCredential;
import nabhack.localz.models.DeviceRegisterRequest;
import nabhack.localz.models.DeviceSettings;
import nabhack.localz.models.Filter;
import nabhack.localz.models.Notification;
import nabhack.localz.utils.GCMServerUtilities;
import nabhack.localz.webservice.WebServiceController;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
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

	DealAdapter dealAdapter;

	List<Deal> filteredDeals;

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
	private static final String PROPERTY_ON_SERVER_DEVICE_ID = "haruku_device_id";
	private static final String PROPERTY_ON_SERVER_DEVICE_KEY = "haruku_device_key§";
	
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

		// Test
		// Notification notification1 = new Notification("a123asdfj234ubsd323s",
		// true);
		// Filter filter1 = new Filter(new ArrayList<String>(), new
		// ArrayList<String>());
		// DeviceRegisterRequest request = new
		// DeviceRegisterRequest(notification1, "Android", "4.3.1",
		// "0412312312", "0.1", "520a01e1c5b56578f6000008", filter1);
		// DeviceCredential r =
		// WebServiceController.getInstance().deviceRegister(request);
		//
		// DeviceCredential deviceCredential = new
		// DeviceCredential(r.getDeviceId(), r.getDeviceKey());
		// BasicResponse b1 =
		// WebServiceController.getInstance().deviceSignIn(deviceCredential);
		//
		// DeviceSettings s =
		// WebServiceController.getInstance().getDeviceSettings(r.getDeviceId());
		//
		// Notification notification2 = new Notification(null, false);
		// Filter filter2 = new Filter(new ArrayList<String>(), new
		// ArrayList<String>());
		// DeviceSettings deviceSettings = new DeviceSettings(notification2,
		// filter2);
		// BasicResponse b2 =
		// WebServiceController.getInstance().postDeviceSettings(r.getDeviceId(),
		// deviceSettings);

		gcmRegid = getRegistrationId(getApplicationContext());

		if (gcmRegid.length() == 0) {
			registerBackground();
		}
		gcmServer = GoogleCloudMessaging.getInstance(this);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		Log.i(TAG, "in onResume() method");
		Intent intent = getIntent();

		// Android Beam mode
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
			NdefMessage[] msgs = getNdefMessages(intent);
			String payload = new String(msgs[0].getRecords()[0].getPayload());

			// the payload is the store id
			// Chris please handle it
			// (call backend to retrieve deal, etc.)
			Log.d(TAG, "Received NDEF message " + payload);

			// this is response to checkin NFC tag.
			// NFC tag contains message "storeid:123456789"
			// storeid really means "shopping center id"
			// We need to
			// 1) parse this message and get storeid value
			// 2) using this value call server to retreive top/current deals for
			// this storeid
			// for this customer because server will know customer's filter
			// (or do local retrieval for demo)
			// 3) then set filteredDeals to the deals retrieved from server
			// for now just
			// filteredDeals = application.getDealsOnOffer();
			// setupListBasedOnFilter();

			filteredDeals = application.getDealsOnOffer();
			setupListBasedOnFilter();

		}
		

		// GCM
		
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("nabhack.localz");
		notificationReceiver = new NotificationBroadcastReceiver();
		registerReceiver(notificationReceiver, intentFilter);
		
		super.onResume();
	}

	/**
	 * Gets the NFC message.
	 * 
	 * @param intent
	 *            intent used for nfc.
	 * @return nfc message.
	 */
	protected NdefMessage[] getNdefMessages(Intent intent) {
		// Parse the intent
		NdefMessage[] msgs = null;
		String action = intent.getAction();

		if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
				|| NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
			Parcelable[] rawMsgs = intent
					.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
			if (rawMsgs != null) {
				msgs = new NdefMessage[rawMsgs.length];
				for (int i = 0; i < rawMsgs.length; i++) {
					msgs[i] = (NdefMessage) rawMsgs[i];
				}
			} else {
				Log.e(TAG, "Invalid NDEF message.");
			}
		} else {
			Log.e(TAG, "Unknown intent.");
		}
		return msgs;
	}

	@AfterViews
	void setupView() {
		// next 2 lines should not really be there, as we do not know which shopping
		// center user is in now
		filteredDeals = application.getDealsOnOffer();
		setupListBasedOnFilter();
		
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

		ImageView menuIconFilter = (ImageView) findViewById(R.id.abs_home_menu_filter);
		menuIconFilter.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DealSummaryActivity.this,
						FilterActivity_.class);
				startActivityForResult(intent, 1);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent arg2) {
		if (resultCode == Activity.RESULT_OK) {
			// return from filter
			setupListBasedOnFilter();
		}
		super.onActivityResult(requestCode, resultCode, arg2);
	}

	private void setupListBasedOnFilter() {
		List<String> checkedCategories = new ArrayList<String>();
		for (Category category : ((LocalzApp) getApplication()).getCategories()) {
			if (category.isChecked()) {
				checkedCategories.add(category.getDealCategory());
				Log.d(TAG, "Checked Categories: " + category.getDealCategory());
			}
		}

		filteredDeals = new ArrayList<Deal>();
		for (Deal deal : ((LocalzApp) getApplication()).getDealsOnOffer()) {
			String[] dealCategories = deal.getCategories();
			Log.d(TAG, "dealCategories: " + dealCategories);
			for (String dCategory : dealCategories) {
				if (checkedCategories.contains(dCategory)) {
					filteredDeals.add(deal);
					break;
				}
			}
		}
		Log.d(TAG, "filtered list size: " + filteredDeals.size());
		dealAdapter = new DealAdapter(this, R.layout.deal_list_item,
				R.id.title, filteredDeals);
		listView.setAdapter(dealAdapter);
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
	private void setGCMRegistrationId(Context context, String regId, DeviceCredential deviceCredential) {
		final SharedPreferences prefs = getGCMPreferences(context);
		Log.v(TAG, "Saving GCM regId " + regId + " to shared preferences");
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(PROPERTY_REG_ID, regId);
		long expirationTime = System.currentTimeMillis()
				+ REGISTRATION_EXPIRY_TIME_MS;

		Log.v(TAG, "Setting registration expiry time to "
				+ new Timestamp(expirationTime));
		editor.putLong(PROPERTY_ON_SERVER_EXPIRATION_TIME, expirationTime);
		editor.putString(PROPERTY_ON_SERVER_DEVICE_ID, deviceCredential.getDeviceId());		
		editor.putString(PROPERTY_ON_SERVER_DEVICE_KEY, deviceCredential.getDeviceKey());		
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

					Notification notification = new Notification(
							gcmRegid, true);
					Filter filter = new Filter(new ArrayList<String>(),
							new ArrayList<String>());
					DeviceRegisterRequest request = new DeviceRegisterRequest(
							notification, "Android", "4.3.1", "0412312312",
							"0.1", "520a01e1c5b56578f6000008", filter);
					DeviceCredential deviceCredential = WebServiceController.getInstance()
							.deviceRegister(request);

					// Save the regid to SharedPreferences - no need to register
					// again.
					setGCMRegistrationId(getApplicationContext(), gcmRegid, deviceCredential);
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
			Log.d(TAG, "onReceive()");
			// Here need to pull new deal, etc. leave it to Chris
			AlertDialog.Builder builder = new AlertDialog.Builder(
					DealSummaryActivity.this);
			// Call server to retrieve deal deatails.
			// getString("Message") should contain dealid
			builder.setTitle("New Localz Deal!")
					.setMessage(
							"New Deal has just been released. Details of the deail......")
					.setOnCancelListener(
							new DialogInterface.OnCancelListener() {

								@Override
								public void onCancel(DialogInterface dialog) {
									dialog.dismiss();

								}
							})
					.setNeutralButton("Get Deal",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// value 5 is hardcoded, need to put actual
									// value of
									// the deal
									application.setCurrentDeal((Deal) listView
											.getItemAtPosition(5));
									Intent intent = new Intent(
											DealSummaryActivity.this,
											DealDetailsActivity_.class);
									startActivity(intent);
									dialog.dismiss();

								}
							}).create().show();

			Toast.makeText(context, intent.getExtras().getString("Message"),
					Toast.LENGTH_LONG).show();
		}
	}
}
