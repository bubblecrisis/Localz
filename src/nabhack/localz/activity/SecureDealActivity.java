package nabhack.localz.activity;

import nabhack.localz.LocalzApp;
import nabhack.localz.R;
import nabhack.localz.models.Deal;
import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;
import com.nostra13.universalimageloader.core.ImageLoader;

@EActivity(R.layout.activity_secure_deal)
public class SecureDealActivity extends Activity {

	public static final String DEALID_INTENT_EXTRAS = "dealid";
	private static final String TAG = "SecureDealActivity";
	@App
	LocalzApp application;

	@ViewById(R.id.secure_deal_image)
	ImageView image;

	@ViewById(R.id.secure_deal_details)
	TextView details;

	@ViewById(R.id.secure_deal_remaining)
	TextView remaining;
	
	String dealid="123";

	public SecureDealActivity() {
	}

	@AfterViews
	void setupView() {
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
			Toast.makeText(this, "Received NDEF message " + payload,
					Toast.LENGTH_LONG);

			// set deal id from NFC message
			// dealid = getDealIdFromNFCPayload(payload);
			application.setCurrentDeal(application.getDeal(3));
			Deal deal = application.getCurrentDeal();
			details.setText(deal.getDescription());
			ImageLoader.getInstance()
					.displayImage(deal.getDescImgs()[0], image);
			remaining.setText(deal.getQuantityLimit() + " Remaining"); // TODO:
																		// Include
																		// time
																		// remaining

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

	void secureDeal() {
		Intent intent = new Intent(SecureDealActivity.this, RedeemActivity_.class);
		Bundle extras = new Bundle();
		extras.putString(DEALID_INTENT_EXTRAS, dealid);
		intent.putExtras(extras);
		startActivity(intent);
	}
}
