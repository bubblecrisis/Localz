package nabhack.localz.activity;

import nabhack.localz.LocalzApp;
import nabhack.localz.R;
import nabhack.localz.models.Deal;
import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;
import com.nostra13.universalimageloader.core.ImageLoader;

@EActivity(R.layout.activity_secure_deal)
public class SecureDealActivity extends Activity {

	@App
	LocalzApp application;

	@ViewById(R.id.image)
	ImageView image;

	@ViewById(R.id.details)
	TextView details;
	
	@ViewById(R.id.remaining)
	TextView remaining;

	public SecureDealActivity() {
	}

	@AfterViews
	void setupView() {
		Deal deal = application.getCurrentDeal();
		details.setText(deal.getDescription());
		ImageLoader.getInstance().displayImage(deal.getDescImgs()[0], image);
		remaining.setText(deal.getQuantityLimit() + " Remaining"); // TODO: Include time remaining
	}

}
