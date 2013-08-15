package nabhack.localz.activity;

import nabhack.localz.R;
import android.app.Activity;

import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
@EActivity(R.layout.activity_redeem) 
public class RedeemActivity extends Activity {

	@Extra(SecureDealActivity.DEALID_INTENT_EXTRAS)
	String dealid;


}
