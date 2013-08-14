
package nabhack.localz.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;

import com.google.zxing.WriterException;
import com.google.zxing.client.android.Intents;
import com.google.zxing.client.android.encode.QRCodeEncoder;

public final class ImageUtils {
	/**
	 * Private constructor.
	 */
	private ImageUtils() {

	}

	public static Bitmap generateQRCode(String id, int dimension, Activity activity) throws WriterException {
		
		// Setup intent to generate QR code.
		Intent intent = new Intent(Intents.Encode.ACTION);
		intent.putExtra("ENCODE_TYPE", "TEXT_TYPE");
		intent.putExtra("ENCODE_DATA", id);

		QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(activity, intent, dimension, false);
		Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
		return bitmap;
	}

}
