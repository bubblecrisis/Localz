package nabhack.localz.activity;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;


import nabhack.localz.LocalzApp;
import nabhack.localz.R;
import nabhack.localz.activity.FacebookFragment.IFacebookSessionCallback;
import nabhack.localz.models.Deal;
import nabhack.localz.ui.SimpleTextDialog;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.IntentSender;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.FragmentById;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;

@OptionsMenu(R.menu.deal_details)
@EActivity(R.layout.activity_deal_details)
public class DealDetailsActivity extends FragmentActivity implements
		IFacebookSessionCallback, GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {

	private static final String TAG = DealDetailsActivity.class.getSimpleName();

	private LocationClient mLocationClient;

	/*
	 * Define a request code to send to Google Play services This code is
	 * returned in Activity.onActivityResult
	 */
	private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

	@App
	LocalzApp application;

	FacebookFragment facebookFragment;

	boolean isLoggingIn;

	boolean isNewFacebookLogin;

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter sectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	@ViewById(R.id.pager)
	ViewPager viewPager;

	@FragmentById(R.id.shop_map)
	SupportMapFragment mapFragment;

	private GoogleMap mMap;

	@Override
	protected void onCreate(Bundle arg0) {
		mLocationClient = new LocationClient(this, this, this);
		super.onCreate(arg0);
	}

	@AfterViews
	protected void setupView() {
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		sectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());
		viewPager.setAdapter(sectionsPagerAdapter);

		// Find the current chosen deal
		viewPager.setCurrentItem(application.getDealsOnOffer().indexOf(
				application.getCurrentDeal()));

		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			public void onPageSelected(int position) {

				application.setCurrentDeal(application.getDeal(position));
				moveCameraToPositionAndAddMarker();

			}

			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			public void onPageScrollStateChanged(int arg0) {

			}
		});

		setUpMapIfNeeded();
		setUpMap();

		facebookFragment = new FacebookFragment_();
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.add(R.id.pager, facebookFragment);
		ft.commit();
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {

		if(item.getItemId() == R.id.action_share) {
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
		.build(DealDetailsActivity.this);

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
	

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			DealDetailsFragment fragment = new DealDetailsFragment_();
			fragment.setDeal(application.getDeal(position));
			return fragment;
		}

		@Override
		public int getCount() {
			return application.getDealsOnOffer().size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return getDeal(position).getTitle();
		}

		private Deal getDeal(int position) {
			return application.getDealsOnOffer().get(position);
		}
	}

	@Override
	public void onFacebookSessionStateChange(com.facebook.Session session,
			SessionState state, Exception exception) {
		if (isLoggingIn) {
			if (state.equals(SessionState.OPENING)) {
				isLoggingIn = false;
			}
			return;
		}

		if (session.isOpened()) {
			if (isNewFacebookLogin) {
				postDealOnFacebook();
				isNewFacebookLogin = false;
			}		
		} else if (session.isClosed()) {
			if (!isNewFacebookLogin && exception != null) {
				new SimpleTextDialog.Builder().cancelButtonVisible(false)
						.header(R.string.facebook_error_title)
						.body(exception.getMessage()).build(this).show();
			}
		}

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
			postParams.putString("link","https://developers.facebook.com/android");
			postParams.putString("picture","https://raw.github.com/fbsamples/ios-3.x-howtos/master/Images/iossdk_logo.png");

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
		Toast.makeText(DealDetailsActivity.this, "Deal shared on Facebook", Toast.LENGTH_SHORT).show();
	}
	
	private void setUpMapIfNeeded() {
		if (mMap == null) {
			mMap = mapFragment.getMap();
			mMap.setLocationSource(new MockLocationSource());
		}
	}

	private void setUpMap() {
		mMap.setIndoorEnabled(true);
		mMap.setMyLocationEnabled(true);

		application.setCurrentDeal(application.getDeal(0));
		moveCameraToPositionAndAddMarker();
	}

	private void moveCameraToPositionAndAddMarker() {
		mMap.clear();
		LatLng dealLatLng = new LatLng(application.getCurrentDeal()
				.getLocation().getLat(), application.getCurrentDeal()
				.getLocation().getLng());

		mMap.addMarker(
				new MarkerOptions().position(dealLatLng).title(
						application.getCurrentDeal().getTitle()))
				.showInfoWindow();

		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(dealLatLng).zoom(18).tilt(60) // Sets the tilt of the
														// camera
														// to 60 degrees
				.build(); // Creates a CameraPosition from the builder
		mMap.animateCamera(CameraUpdateFactory
				.newCameraPosition(cameraPosition));
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		Log.w(TAG, "Failed to connect to Google Play services");
		/*
		 * Google Play services can resolve some errors it detects. If the error
		 * has a resolution, try sending an Intent to start a Google Play
		 * services activity that can resolve error.
		 */
		if (connectionResult.hasResolution()) {
			try {
				// Start an Activity that tries to resolve the error
				connectionResult.startResolutionForResult(this,
						CONNECTION_FAILURE_RESOLUTION_REQUEST);
				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
			} catch (IntentSender.SendIntentException e) {
				// Log the error
				Log.w(TAG,
						"Failed to connect to Google Play services"
								+ e.getMessage());
			}
		}
	}

	@Override
	public void onConnected(Bundle arg0) {
		mLocationClient.setMockMode(true);
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

	public class MockLocationSource implements LocationSource {


		private final Handler handler = new Handler();
		private OnLocationChangedListener listener;

		private void scheduleNewFix() {
			handler.postDelayed(updateLocationRunnable,
					TimeUnit.SECONDS.toMillis(2));
		}

		private final Runnable updateLocationRunnable = new Runnable() {

			@Override
			public void run() {
				android.location.Location randomLocation = generateRandomLocation();
				listener.onLocationChanged(randomLocation);
				scheduleNewFix();
			}
		};

		public android.location.Location generateRandomLocation() {

			android.location.Location mockedLocation = new android.location.Location(
					getClass().getSimpleName());

			mockedLocation.setLatitude(-37.885795);
			mockedLocation.setLongitude(145.083812);
			mockedLocation.setAccuracy(1);

			return mockedLocation;
		}

		@Override
		public void activate(OnLocationChangedListener locationChangedListener) {
			listener = locationChangedListener;
			scheduleNewFix();
		}

		@Override
		public void deactivate() {
			handler.removeCallbacks(updateLocationRunnable);
		}

	}
}
