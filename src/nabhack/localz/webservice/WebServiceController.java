package nabhack.localz.webservice;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class WebServiceController {

	private static final String URL_BASE = "http://dev-api-localz.herokuapp.com";
	private static final String URL_DEVICE_REGISTER = "/register";
	private static final String URL_DEVICE_SIGN_IN = "/deviceSignIn";
	private static final String URL_DEVICE_SETTINGS = "/devices/{deviceId}";

	private static WebServiceController instance;
	private HttpClientManager httpClientManager;

	public static WebServiceController getInstance() {
		if (instance == null) {
			instance = new WebServiceController();
		}
		return instance;
	}

	private WebServiceController() {
		httpClientManager = new HttpClientManager();
	}

	private Map<String, String> processServerResponse(int responseCode,
			String response) {

		if ((responseCode >= HttpURLConnection.HTTP_OK)
				&& (responseCode < HttpURLConnection.HTTP_MULT_CHOICE)) {
			return null;
		}

		Map<String, String> map = new HashMap<String, String>();
		try {
			JSONObject jsonObject = new JSONObject(response);
			Iterator keys = jsonObject.keys();
			while (keys.hasNext()) {
				String key = (String) keys.next();
				map.put(key, jsonObject.getString(key));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return map;
	}

	public Map<String, String> deviceRegister(String pushId,
			boolean isPushEnabled, String mobileOS, String mobileOSVer,
			String mobileNumber, String appVer, String area,
			Map<String, String> filter) {

		String url = URL_BASE + URL_DEVICE_REGISTER;
		int serverResponse = HttpURLConnection.HTTP_OK;
		StringBuffer responseBuffer = new StringBuffer();

		String params = "{" + "notification:" + "{" + "pushId:\"" + pushId
				+ "\"," + "isPushEnabled:" + isPushEnabled + "},"
				+ "mobileOS:\"" + mobileOS + "\"," + "mobileOSVer:\""
				+ mobileOSVer + "\"," + "mobileNumber:\"" + mobileNumber
				+ "\"," + "appVer:\"" + appVer + "\"," + "area:\"" + area
				+ "\"," + "filter:\"" + filter.toString() + "\"}}";

		try {
			serverResponse = this.httpClientManager.connectToWebServer(url,
					HttpClientManager.HTTP_METHOD.POST, params, responseBuffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return processServerResponse(serverResponse, responseBuffer.toString());
	}

	public Map<String, String> deviceSignIn(String deviceId, String deviceKey) {

		String url = URL_BASE + URL_DEVICE_SIGN_IN;
		int serverResponse = HttpURLConnection.HTTP_OK;
		StringBuffer responseBuffer = new StringBuffer();

		String params = "{deviceId:\"" + deviceId + "\",deviceKey:\""
				+ deviceKey + "\"}";

		try {
			serverResponse = this.httpClientManager.connectToWebServer(url,
					HttpClientManager.HTTP_METHOD.POST, params, responseBuffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return processServerResponse(serverResponse, responseBuffer.toString());
	}

	public Map<String, String> getDeviceSettings(String deviceId,
			boolean isPushEnabled, Map<String, String> filter) {

		String url = URL_BASE
				+ URL_DEVICE_SETTINGS.replace("{deviceId}", deviceId);
		int serverResponse = HttpURLConnection.HTTP_OK;
		StringBuffer responseBuffer = new StringBuffer();

		try {
			serverResponse = this.httpClientManager.connectToWebServer(url,
					HttpClientManager.HTTP_METHOD.POST, null, responseBuffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return processServerResponse(serverResponse, responseBuffer.toString());
	}

	public Map<String, String> postDeviceSettings(String deviceId,
			boolean isPushEnabled, String pushId, Map<String, String> filter) {

		String url = URL_BASE
				+ URL_DEVICE_SETTINGS.replace("{deviceId}", deviceId);
		int serverResponse = HttpURLConnection.HTTP_OK;
		StringBuffer responseBuffer = new StringBuffer();

		String params = "{notification:{isPushEnabled:" + isPushEnabled + ","
				+ "pushId:\"" + pushId + ",},filter:{" + filter.toString()
				+ "}}";

		try {
			serverResponse = this.httpClientManager.connectToWebServer(url,
					HttpClientManager.HTTP_METHOD.POST, params, responseBuffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return processServerResponse(serverResponse, responseBuffer.toString());
	}
}
