package nabhack.localz.activity;

import nabhack.localz.LocalzApp;
import nabhack.localz.R;
import nabhack.localz.models.Deal;
import nabhack.localz.ui.ImageUtils;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.WriterException;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.Extra;
import com.googlecode.androidannotations.annotations.SystemService;
import com.googlecode.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_redeem)

public class RedeemActivity extends Activity {
	
	@App
	LocalzApp application;
	
	@SystemService
	WindowManager windowsManager;
	
	@ViewById(R.id.qrcode_image_view)
	ImageView qrImageView;

	@ViewById(R.id.image)
	ImageView image;
	
	@ViewById(R.id.deal_title)
	TextView dealTitle;
	
	@Extra(SecureDealActivity_old.DEALID_INTENT_EXTRAS)
	String dealid;
	
	@AfterViews
	void initDisplay() {
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		application.setCurrentDeal(application.getDeal(3));
		Deal deal = application.getCurrentDeal();
		createQRCode(application.getCurrentDeal().getTitle());
		dealTitle.setText(application.getCurrentDeal().getTitle());
		String uri = "drawable/"
				+ deal.getDescImgs()[0].replaceFirst("[.][^.]+$", "");
		int imageResource = getResources().getIdentifier(uri, null,getPackageName());
		Drawable drawImage = getResources().getDrawable(imageResource);
		image.setImageDrawable(drawImage);
		
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
