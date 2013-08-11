package nabhack.localz.activity;

import nabhack.localz.LocalzApp;
import nabhack.localz.R;
import nabhack.localz.models.Deal;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.EActivity;
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
			}
			public void onPageScrolled(int arg0, float arg1, int arg2) { }
			public void onPageScrollStateChanged(int arg0) { }
		});
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
}
