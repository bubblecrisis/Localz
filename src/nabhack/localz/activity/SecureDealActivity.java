package nabhack.localz.activity;

import nabhack.localz.LocalzApp;
import nabhack.localz.R;
import nabhack.localz.models.Deal;
import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.webkit.WebView;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_secure_deal)
public class SecureDealActivity extends Activity {

	@App
	LocalzApp application;

	@ViewById(R.id.image)
	WebView image;

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
		image.loadUrl(deal.getDescImgs()[0]);
		remaining.setText(deal.getQuantityLimit() + " Remaining"); // TODO: Include time remaining
	}

}
