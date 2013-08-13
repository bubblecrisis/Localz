package nabhack.localz.activity;

import nabhack.localz.LocalzApp;
import nabhack.localz.R;
import nabhack.localz.models.Deal;
import nabhack.localz.models.Location;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.FragmentById;
import com.googlecode.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_deal_details)
public class DealDetailsActivity extends FragmentActivity {

	@App
	LocalzApp application;
	
	
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
		sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(sectionsPagerAdapter);
		
		// Find the current chosen deal
		viewPager.setCurrentItem(application.getDealsOnOffer().indexOf(application.getCurrentDeal()));
		
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			public void onPageSelected(int position) {
				application.setCurrentDeal(application.getDeal(position)); 
				moveCameraToPositionAndAddMarker();
			}
			public void onPageScrolled(int arg0, float arg1, int arg2) { }
			public void onPageScrollStateChanged(int arg0) { }
		});
		
		setUpMapIfNeeded();
		setUpMap();
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
		LatLng dealLatLng = new LatLng(application.getCurrentDeal().getAltClaimLocation().getLat(),
				application.getCurrentDeal().getAltClaimLocation().getLng());

		mMap.addMarker(new MarkerOptions().position(dealLatLng).title(
				application.getCurrentDeal().getTitle())).showInfoWindow();

		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(dealLatLng).zoom(5).tilt(60) // Sets the tilt of the
														// camera
														// to 60 degrees
				.build(); // Creates a CameraPosition from the builder
		mMap.animateCamera(CameraUpdateFactory
				.newCameraPosition(cameraPosition));
	}
}
