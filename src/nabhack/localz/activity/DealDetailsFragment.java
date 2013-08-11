package nabhack.localz.activity;

import nabhack.localz.LocalzApp;
import nabhack.localz.R;
import nabhack.localz.models.Deal;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.App;
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
	
	@ViewById(R.id.remaining)
	TextView remaining;

	public DealDetailsFragment() {
	}

	@AfterViews
	void setupView() {
		Deal deal = application.getCurrentDeal();
		details.setText(deal.getDescription());
		ImageLoader.getInstance().displayImage(deal.getDescImgs()[0], image);
		remaining.setText(deal.getQuantityLimit() + " Remaining"); // TODO: Include time remaining
		fixNoRectBasedTestNodeFoundProblem();
	}


	/**
	 * Getting this:
	 * "E/webcoreglue(21690): Should not happen: no rect-based-test nodes found"
	 * in log See
	 * http://stackoverflow.com/questions/12090899/android-webview-jellybean
	 * -should-not-happen-no-rect-based-test-nodes-found
	 */
	void fixNoRectBasedTestNodeFoundProblem() {
		image.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View view, MotionEvent event) {

				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					int temp_ScrollY = view.getScrollY();
					view.scrollTo(view.getScrollX(), view.getScrollY() + 1);
					view.scrollTo(view.getScrollX(), temp_ScrollY);
				}

				return false;
			}
		});
	}
}