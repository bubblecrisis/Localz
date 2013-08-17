package nabhack.localz.activity;

import nabhack.localz.R;
import android.app.Activity;
import android.content.Intent;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.UiThread;

@EActivity(R.layout.activity_front_page)
public class FrontpageActivity extends Activity {

	@AfterViews
	void initDisplay() {
		loading();
	}
	
	@Background
	void loading() {
		try {
			Thread.sleep(2000);
			displayMainPage();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@UiThread
	void displayMainPage() {
		Intent intent = new Intent(FrontpageActivity.this, DealSummaryActivity_.class);
		startActivity(intent);
		finish();
	}
	
}
