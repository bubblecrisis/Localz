       package nabhack.localz.activity;

import nabhack.localz.LocalzApp;
import nabhack.localz.R;
import nabhack.localz.activity.DealDetailsFragment.Callback;
import nabhack.localz.models.Deal;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.NetworkInfo.DetailedState;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_secure_deal)
public class SecureDealActivity_old extends FragmentActivity implements Callback{

	public static final String DEALID_INTENT_EXTRAS = "dealid";
	private static final String TAG = "SecureDealActivity";
	@App
	LocalzApp application;
	
	@ViewById(R.id.deals_fragment)
	LinearLayout dealLayout;

	@ViewById(R.id.deal_title)
	TextView dealTitle;
	
	String dealid;
	
	DealDetailsFragment dealDetailsFragment;

	public SecureDealActivity_old() {
	}

	@AfterViews
	void setupView() {	
		dealDetailsFragment = new DealDetailsFragment_();
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.add(R.id.deals_fragment, dealDetailsFragment);
		ft.commit();
		application.setCurrentDeal(application.getDeal(3));	
		dealDetailsFragment.setDeal(application.getCurrentDeal());
		
	}

	@Override
	protected void onResume() {
		Log.i(TAG, "in onResume() method");
		Intent intent = getIntent();

		// Android Beam mode
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
			NdefMessage[] msgs = getNdefMessages(intent);
			String payload = new String(msgs[0].getRecords()[0].getPayload());

			// the payload is the store id
			// Chris please handle it
			// (call backend to retrieve deal, etc.)
			Log.d(TAG, "Received NDEF message " + payload);

			// set deal id from NFC message
			// dealid = getDealIdFromNFCPayload(payload);
			//dealid:1234567890
			// NFC tag contains message "dealid:1234567890"
			// We need to 
			// 1) parse this message and get dealid value
			// 2) using this value call server to retreive deal deatails
			// (or do local retrieval for demo)
			// 3) then set application.setCurrentDeal(deal from step 2));
			// for now just retrieving deal [3] comments this line, once 1)-3) done.		
			Deal deal = application.getCurrentDeal();
			dealTitle.setText(deal.getTitle());
			
			// Uncomment when data will be available online
			//ImageLoader.getInstance().displayImage(deal.getDescImgs()[0], image);

			// Comment next block when data available online
			String uri = "drawable/"
					+ deal.getDescImgs()[0].replaceFirst("[.][^.]+$", "");
			int imageResource = getResources().getIdentifier(uri, null,
				getPackageName());
		}
		super.onResume();
	}

	/**
	 * Gets the NFC message.
	 * 
	 * @param intent
	 *            intent used for nfc.
	 * @return nfc message.
	 */
	protected NdefMessage[] getNdefMessages(Intent intent) {
		// Parse the intent
		NdefMessage[] msgs = null;
		String action = intent.getAction();

		if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
				|| NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
			Parcelable[] rawMsgs = intent
					.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
			if (rawMsgs != null) {
				msgs = new NdefMessage[rawMsgs.length];
				for (int i = 0; i < rawMsgs.length; i++) {
					msgs[i] = (NdefMessage) rawMsgs[i];
				}
			} else {
				Log.e(TAG, "Invalid NDEF message.");
			}
		} else {
			Log.e(TAG, "Unknown intent.");
		}
		return msgs;
	}

	@Click(R.id.secure_deal)
	void secureDeal() {
		Intent intent = new Intent(SecureDealActivity_old.this, RedeemActivity_.class);
		Bundle extras = new Bundle();
		extras.putString(DEALID_INTENT_EXTRAS, dealid);
		intent.putExtras(extras);
		startActivity(intent);
	}

	@Override
	public void initDisplay() {
		dealDetailsFragment.showArrows(false);	
	}
}
