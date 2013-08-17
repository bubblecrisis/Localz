package nabhack.localz.activity;

import nabhack.localz.LocalzApp;
import nabhack.localz.R;
import nabhack.localz.models.Deal;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.widget.ImageView;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;

/**
 * A dummy fragment representing a section of the app, but that simply displays
 * dummy text.
 */
@EFragment(R.layout.fragment_deal_details)
public class DealDetailsFragment extends Fragment {

	@App
	LocalzApp application;

	@ViewById(R.id.image)
	ImageView image;

	@ViewById(R.id.details)
	TextView details;

	@ViewById(R.id.title)
	TextView title;

	@ViewById(R.id.remaining)
	TextView remaining;

	private Deal deal;

	public DealDetailsFragment() {
	}

	public void setDeal(Deal deal) {
		this.deal = deal;
	}

	@AfterViews
	void setupView() {
		details.setText(deal.getDescription());
		//title.setText(deal.getTitle());

		// Uncomment when data will be available online
		// ImageLoader.getInstance().displayImage(deal.getDescImgs()[0], image);

		// Comment next block when data available online
		String uri = "drawable/"
				+ deal.getDescImgs()[0].replaceFirst("[.][^.]+$", "");
		int imageResource = getResources().getIdentifier(uri, null,
				getActivity().getPackageName());
		Drawable drawImage = getActivity().getResources().getDrawable(
				imageResource);
		image.setImageDrawable(drawImage);

		remaining.setText(deal.getQuantityLimit() + " Remaining"); // TODO:
																	// Include
																	// time
																	// remaining
	}

}