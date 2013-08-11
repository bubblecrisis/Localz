package nabhack.localz;

import java.util.ArrayList;
import java.util.List;

import nabhack.localz.models.Deal;
import android.app.Application;

import com.googlecode.androidannotations.annotations.EApplication;

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
		// Setup Mock Data
		// -------------------------------
		
		final String IMAGE_URL = "http://apcmag.com/images/2011/Top3TabletSamsungGalaxyTabAd.jpg";
		final String DESC = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
		
		dealsOnOffer.add(new Deal("American Bombshell B-10 Warhead", "10%", IMAGE_URL, DESC));
		dealsOnOffer.add(new Deal("The Great American Challenge", "11%", IMAGE_URL, DESC));
		dealsOnOffer.add(new Deal("Belladonna's Magic Hand", "12%", IMAGE_URL, DESC));
		dealsOnOffer.add(new Deal("Gum Drops Ace of Spades", "13%", IMAGE_URL, DESC));
		dealsOnOffer.add(new Deal("TitanMen Intimidator", "14%", IMAGE_URL, DESC));
		dealsOnOffer.add(new Deal("Gum Drops Ace of Spades", "15%", IMAGE_URL, DESC));
		dealsOnOffer.add(new Deal("Master Series Dark Nadir", "16%", IMAGE_URL, DESC));		
	}
	/**
	 * @return the dealsOnOffer
	 */
	public List<Deal> getDealsOnOffer() {
		return dealsOnOffer;
	}
}
