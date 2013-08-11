package nabhack.localz.activity;

import nabhack.localz.LocalzApp;
import nabhack.localz.R;
import nabhack.localz.adapter.DealAdapter;
import nabhack.localz.models.Deal;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.widget.ListView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ItemClick;
import com.googlecode.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_deal_summary)
public class DealSummaryActivity extends Activity {


	@ViewById
	ListView listView;

	@App
	LocalzApp application;

	
	@AfterViews
    void setupView() {
		listView.setAdapter(new DealAdapter(this, R.layout.deal_list_item, R.id.title, application.getDealsOnOffer()));  

		
    }
	
	@ItemClick(R.id.listView)
	void listViewClicked(int position) {
		application.setCurrentDeal((Deal) listView.getItemAtPosition(position));
		Intent intent = new Intent(this, DealDetailsActivity_.class);
		startActivity(intent);
	}
}
