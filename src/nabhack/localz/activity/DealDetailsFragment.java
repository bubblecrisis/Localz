package nabhack.localz.activity;

import nabhack.localz.LocalzApp;
import nabhack.localz.R;
import nabhack.localz.models.Deal;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.Click;
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
	WebView image;  // Should be ImageView. Use WebView to get things going quickly! 

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
		image.loadUrl(deal.getDescImgs()[0]);
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