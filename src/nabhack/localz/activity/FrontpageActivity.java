package nabhack.localz.activity;

import nabhack.localz.R;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_front_page)
public class FrontpageActivity extends Activity {
	
	@ViewById(R.id.localzText)
	TextView localzText;

	@AfterViews
	void initDisplay() {
		loading();
		Typeface rockbFont = Typeface.createFromAsset(this.getAssets(),"rockb.ttf");
		localzText.setTypeface(rockbFont);
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#2d3e50")));
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
