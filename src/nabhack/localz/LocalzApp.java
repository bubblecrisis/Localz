package nabhack.localz;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.client.RestTemplate;

import nabhack.localz.models.Category;
import nabhack.localz.models.Deal;
import nabhack.localz.webservice.WebServiceController;
import android.app.Application;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.googlecode.androidannotations.annotations.EApplication;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

@EApplication
public class LocalzApp extends Application {
	List<Deal> dealsOnOffer = new ArrayList();

	// Working data
	Deal currentDeal;
	
	List<Category> categories;

	/**
	 * @return the currentDeal
	 */
	public Deal getCurrentDeal() {
		if (currentDeal == null) {
			currentDeal = dealsOnOffer.get(3);
		}
		return currentDeal;
	}

	/**
	 * @param currentDeal
	 *            the currentDeal to set
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
		// Create global configuration and initialize ImageLoader with this
		// configuration
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext()).writeDebugLogs()
				.defaultDisplayImageOptions(new DisplayImageOptions.Builder()
				// .showStubImage(R.drawable.ic_stub)
				// .showImageForEmptyUri(R.drawable.ic_empty)
				// .showImageOnFail(R.drawable.ic_error)
						.delayBeforeLoading(0).cacheInMemory(true)
						// .cacheOnDisc(false) // default
						.build()).build();
		ImageLoader.getInstance().init(config);

		// -------------------------------
		// Setup Mock Data
		// -------------------------------

		final String IMAGE_URL = "http://apcmag.com/images/2011/Top3TabletSamsungGalaxyTabAd.jpg";
		final String DESC = "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
		
		dealsOnOffer = WebServiceController.getInstance().getDeals("latest");

		categories = new ArrayList<Category>();
		categories.add(new Category("Electronics", true));
		categories.add(new Category("Entetainment", true));
		categories.add(new Category("Fashion", true));
		categories.add(new Category("Cosmetics", true));
		categories.add(new Category("Ladies", true));
		categories.add(new Category("Shoes", true));
		categories.add(new Category("Food", true));
		categories.add(new Category("Cafes & Restaurants", true));
		categories.add(new Category("Cinema", true));
		
		/*
		 * dealsOnOffer.add(new Deal("American Bombshell B-10 Warhead", "10%",
		 * IMAGE_URL, DESC, -37.95f, 144.30f)); dealsOnOffer.add(new
		 * Deal("The Great American Challenge", "11%", IMAGE_URL, DESC, -100.1f,
		 * 100.0f)); dealsOnOffer.add(new Deal("Belladonna's Magic Hand", "12%",
		 * IMAGE_URL, DESC, -144.30f, 37.95f)); dealsOnOffer.add(new
		 * Deal("Gum Drops Ace of Spades", "13%", IMAGE_URL, DESC, 38.9f,
		 * 150.8f)); dealsOnOffer.add(new Deal("TitanMen Intimidator", "14%",
		 * IMAGE_URL, DESC, 0.0f, 0.0f)); dealsOnOffer.add(new
		 * Deal("Gum Drops Ace of Spades", "15%", IMAGE_URL, DESC, 18.9f,
		 * 150.8f)); dealsOnOffer.add(new Deal("Master Series Dark Nadir",
		 * "16%", IMAGE_URL, DESC, 30.5f, 100.5f));
		 */
		
		RestTemplate rt = new RestTemplate();
		System.err.println(rt.headForHeaders("http://www.google.com"));
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

	public List<Deal> readJsonStream(InputStream in) throws IOException {
		byte[] buffer = new byte[10000];
		int readBytes = in.read(buffer);
		String jsonString = new String(buffer, 0, readBytes);
		List<Deal> deals = new ArrayList<Deal>();
		Gson gson = new Gson();
		deals = Arrays.asList(gson.fromJson(jsonString, Deal[].class));
		
		return deals;
	}

	public List<Category> getCategories() {
		return categories;
	}
}
