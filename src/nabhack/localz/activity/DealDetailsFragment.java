package nabhack.localz.activity;

import nabhack.localz.LocalzApp;
import nabhack.localz.R;
import nabhack.localz.models.Deal;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;
import com.nostra13.universalimageloader.core.ImageLoader;

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
	
	@ViewById(R.id.share_facebook_btn)
	Button faceBookShareBtn;
	
	private Deal deal;

	public DealDetailsFragment() {
	}

	public void setDeal(Deal deal) {
		this.deal = deal;
	}

	@AfterViews
	void setupView() {
		details.setText(deal.getDescription());
		title.setText(deal.getTitle());
		ImageLoader.getInstance().displayImage(deal.getDescImgs()[0], image);
		remaining.setText(deal.getQuantityLimit() + " Remaining"); // TODO: Include time remaining
	}

	@Click(R.id.share_facebook_btn)
	void clickOnShareFaceBookBtn() {
		DealDetailsActivity dealDetailsActivity = (DealDetailsActivity) getActivity();
		dealDetailsActivity.isLoggingIn = true;
		dealDetailsActivity.isNewFacebookLogin = true;
		dealDetailsActivity.facebookFragment.logoff();
		dealDetailsActivity.facebookFragment.loginViaWebDialog();
		//dealDetailsActivity.publishStory();
	}

}