package nabhack.localz;

import java.util.ArrayList;
import java.util.List;

import nabhack.localz.models.Deal;
import nabhack.localz.models.Location;
import android.app.Application;

import com.googlecode.androidannotations.annotations.EApplication;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

@EApplication
public class LocalzApp extends Application {
	List<Deal> 	dealsOnOffer = new ArrayList();
	
	// Working data
	Deal		currentDeal;

	/**
	 * @return the currentDeal
	 */
	public Deal getCurrentDeal() {
		return currentDeal;
	}
	/**
	 * @param currentDeal the currentDeal to set
	 */
	public void setCurrentDeal(Deal currentDeal) {
		this.currentDeal = currentDeal;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		
		// -------------------------------
		// Configure ImageLoader
		// -------------------------------
        // Create global configuration and initialize ImageLoader with this configuration
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
            .writeDebugLogs()
            .defaultDisplayImageOptions(new DisplayImageOptions.Builder()
	            //.showStubImage(R.drawable.ic_stub)
	            //.showImageForEmptyUri(R.drawable.ic_empty)
	            //.showImageOnFail(R.drawable.ic_error)
	            .delayBeforeLoading(0)
	            .cacheInMemory(true)
	            //.cacheOnDisc(false) // default	     
	            .build())
            .build();
        ImageLoader.getInstance().init(config);
		
		// -------------------------------
		// Setup Mock Data
		// -------------------------------
		
		final String IMAGE_URL = "http://apcmag.com/images/2011/Top3TabletSamsungGalaxyTabAd.jpg";
		final String DESC = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
		
		dealsOnOffer.add(new Deal("American Bombshell B-10 Warhead", "10%", IMAGE_URL, DESC, -37.95f, 144.30f));
		dealsOnOffer.add(new Deal("The Great American Challenge", "11%", IMAGE_URL, DESC, -100.1f, 100.0f));
		dealsOnOffer.add(new Deal("Belladonna's Magic Hand", "12%", IMAGE_URL, DESC, -144.30f, 37.95f));
		dealsOnOffer.add(new Deal("Gum Drops Ace of Spades", "13%", IMAGE_URL, DESC, 38.9f, 150.8f));
		dealsOnOffer.add(new Deal("TitanMen Intimidator", "14%", IMAGE_URL, DESC, 0.0f, 0.0f));
		dealsOnOffer.add(new Deal("Gum Drops Ace of Spades", "15%", IMAGE_URL, DESC, 18.9f, 150.8f));
		dealsOnOffer.add(new Deal("Master Series Dark Nadir", "16%", IMAGE_URL, DESC, 30.5f, 100.5f));		
	}
	
	/**
	 * @return the dealsOnOffer
	 */
	public List<Deal> getDealsOnOffer() {
		return dealsOnOffer;
	}
	
	public Deal getDeal(int position) {
		return dealsOnOffer.get(position);
	}
		
	public int getTotalDealsOnOffer() {
		return dealsOnOffer.size();
	}
}
