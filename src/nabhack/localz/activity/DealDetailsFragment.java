package nabhack.localz.activity;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import nabhack.localz.LocalzApp;
import nabhack.localz.R;
import nabhack.localz.models.Deal;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
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

	
	public interface Callback {

		void initDisplay();
	}
	
	private static final String TAG = DealDetailsFragment.class.getSimpleName();
	
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

	@ViewById(R.id.countdown)
	TextView countdown;
	
	@ViewById(R.id.nextimage)
	ImageView nextImage;
	
	@ViewById(R.id.previmage)
	ImageView prevImage;
	
	int countDownInSeconds;
	
	private Deal deal;
	
	private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

	Callback callback;
	
	public DealDetailsFragment() {
	}

	public void setDeal(Deal deal) {
		this.deal = deal;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof Callback) {
			callback = (Callback) activity;
		}
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
		countDownInSeconds = deal.getSecondsToExpire();
		Log.i(TAG, "count starting :" + countDownInSeconds);
		countdown.setText(getTimeFormat(countDownInSeconds));
		callback.initDisplay();
	}
	
	
	@Override
	public void onResume() {
		runCountDownTimer();
		super.onResume();
	}
	
	@Override
	public void onDestroy() {
		if (scheduledThreadPoolExecutor != null) {
			try {
				scheduledThreadPoolExecutor.shutdown();
				scheduledThreadPoolExecutor.shutdownNow();
			} catch (Exception ex) {
			}
		}
		super.onDestroy();
	}
	
	
	public void showArrows(boolean show) {
		if (show) {
			nextImage.setVisibility(View.VISIBLE);
			prevImage.setVisibility(View.VISIBLE);			
		} else {			
			nextImage.setVisibility(View.INVISIBLE);
			prevImage.setVisibility(View.INVISIBLE);			
		}
	}
	
	public void runCountDownTimer() {
		try {
			scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
			scheduledThreadPoolExecutor.execute(new Runnable() {
				public void run() {
					try {
						while (countDownInSeconds != 0) {		
							Thread.sleep(1000);
							countDownInSeconds--;
							viewUpdateHandler.sendEmptyMessage(0);
							Log.i(TAG, "count :" + countDownInSeconds);
						}
					} catch (InterruptedException ie) {
					}
				}
			});
		} catch (RuntimeException rE) {
		}
	}

	private Handler viewUpdateHandler = new Handler() {

		public void handleMessage(Message msg) {
			countdown.setText(getTimeFormat(countDownInSeconds));	
		}
	};	
	
	
	private String getTimeFormat(int secs) {
		int hours = secs / 3600, 
		remainder = secs % 3600, 
		minutes = remainder / 60, 
		seconds = remainder % 60; 
		
		String disHour = (hours < 10 ? "0" : "") + hours, 
		disMinu = (minutes < 10 ? "0" : "") + minutes , 
		disSec = (seconds < 10 ? "0" : "") + seconds ; 
		return disHour +":"+ disMinu+":"+disSec; 
	}
}