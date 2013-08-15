package nabhack.localz.activity;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import nabhack.localz.LocalzApp;
import nabhack.localz.R;
import nabhack.localz.activity.FacebookFragment.IFacebookSessionCallback;
import nabhack.localz.models.Deal;

import nabhack.localz.models.Location;

import nabhack.localz.ui.SimpleTextDialog;
import nabhack.localz.utils.SessionUtils;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Request.GraphUserCallback;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.FragmentById;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.ViewById;

@OptionsMenu(R.menu.deal_details)
@EActivity(R.layout.activity_deal_details)
public class DealDetailsActivity extends FragmentActivity implements
		IFacebookSessionCallback {

	private static final String TAG = DealDetailsActivity.class.getSimpleName();
	private static final List<String> PERMISSIONS = Arrays
			.asList("publish_actions");
	private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
	private boolean pendingPublishReauthorization = false;

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
			isLoggingIn = true;
			isNewFacebookLogin = true;
			facebookFragment.logoff();
			facebookFragment.loginViaWebDialog();	
		}
		return super.onMenuItemSelected(featureId, item);
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
				publishStory();
				isNewFacebookLogin = false;
			}
			/*
			 * if (pendingPublishReauthorization &&
			 * state.equals(SessionState.OPENED_TOKEN_UPDATED)) {
			 * pendingPublishReauthorization = false; publishStory(); }
			 */
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

	public void publishStory() {
		Session session = Session.getActiveSession();

		if (session != null) {

			// Check for publish permissions
			/*
			 * List<String> permissions = session.getPermissions(); if
			 * (!isSubsetOf(PERMISSIONS, permissions)) {
			 * pendingPublishReauthorization = true;
			 * Session.NewPermissionsRequest newPermissionsRequest = new
			 * Session.NewPermissionsRequest( this, PERMISSIONS);
			 * session.requestNewPublishPermissions(newPermissionsRequest);
			 * return; }
			 */

			Bundle postParams = new Bundle();
			postParams.putString("name", "Facebook SDK for Android");
			postParams.putString("caption",
					"Build great social apps and get more installs.");
			postParams
					.putString(
							"description",
							"The Facebook SDK for Android makes it easier and faster to develop Facebook integrated Android apps.");
			postParams.putString("link",
					"https://developers.facebook.com/android");
			postParams
					.putString("picture",
							"https://raw.github.com/fbsamples/ios-3.x-howtos/master/Images/iossdk_logo.png");

			Request.Callback callback = new Request.Callback() {
				public void onCompleted(Response response) {
					JSONObject graphResponse = response.getGraphObject()
							.getInnerJSONObject();
					String postId = null;
					try {
						postId = graphResponse.getString("id");
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

	private boolean isSubsetOf(Collection<String> subset,
			Collection<String> superset) {
		for (String string : subset) {
			if (!superset.contains(string)) {
				return false;
			}
		}
		return true;
	}

	private void setUpMapIfNeeded() {
		if (mMap == null) {
			mMap = mapFragment.getMap();
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
}
