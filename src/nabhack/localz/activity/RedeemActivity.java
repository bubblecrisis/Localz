package nabhack.localz.activity;

import nabhack.localz.R;
import nabhack.localz.ui.ImageUtils;
import android.app.Activity;
import android.graphics.Bitmap;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.zxing.WriterException;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.SystemService;
import com.googlecode.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_redeem)
public class RedeemActivity extends Activity {

	@SystemService
	WindowManager windowsManager;
	
	@ViewById(R.id.qrcode_image_view)
	ImageView qrImageView;

	@Extra(SecureDealActivity.DEALID_INTENT_EXTRAS)
	String dealid;
	
	@AfterViews
	void initDisplay() {
		//TODO get real deal.
		createQRCode("123");
	}
	
	private void createQRCode(String id) {
		try {
			Display display = windowsManager.getDefaultDisplay();
			int smallerDimension = (int) (display.getHeight() / 2);
			Bitmap bitmap = ImageUtils.generateQRCode(id, smallerDimension, this);
			qrImageView.setImageBitmap(bitmap);
		} catch (WriterException e) {
			
		}
	}

}
