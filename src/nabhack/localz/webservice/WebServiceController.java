package nabhack.localz.webservice;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nabhack.localz.models.BasicResponse;
import nabhack.localz.models.Deal;
import nabhack.localz.models.DeviceCredential;
import nabhack.localz.models.DeviceRegisterRequest;
import nabhack.localz.models.DeviceSettings;
import nabhack.localz.models.ResponseType;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

public class WebServiceController {

	private static final int HTTP_RESPONSE_SESSION_EXPIRED = 403;
	private static final String USER_CREDENTIALS_INVALID = "User credentials invalid";
	private static final String GENERIC_ERROR_MESSAGE = "Internal Server Error Occurred.";

	private static final String URL_BASE = "http://dev-api-localz.herokuapp.com";
	private static final String URL_DEVICE_REGISTER = "/register";
	private static final String URL_DEVICE_SIGN_IN = "/deviceSignIn";
	private static final String URL_DEVICE_SETTINGS = "/devices/{deviceId}";
	private static final String URL_GET_DEALS = "/deals?f={filter}";

	private static WebServiceController instance;
	private HttpClientManager httpClientManager;

	private final Gson gson = new Gson();

	public static WebServiceController getInstance() {
		if (instance == null) {
			instance = new WebServiceController();
		}
		return instance;
	}

	private WebServiceController() {
		httpClientManager = new HttpClientManager();
	}

	private <T> T processServerResponse(int responseCode, String response,
			Class<T> clazz, String dataJsonKey) {
		String dataString = null;
		String responseString = null;

		if ((responseCode >= HttpURLConnection.HTTP_OK)
				&& (responseCode < HttpURLConnection.HTTP_MULT_CHOICE)) {
			if (clazz == null) {
				return null;
			}
		}

		try {
			JSONObject jsonResponse = new JSONObject(response);
			if ((responseCode >= HttpURLConnection.HTTP_OK)
					&& (responseCode < HttpURLConnection.HTTP_MULT_CHOICE)) {
				if (dataJsonKey != null) {
					dataString = jsonResponse.getString(dataJsonKey);
				}
			} else {
				responseString = jsonResponse.getString(ResponseType.JSON_KEY);
			}
		} catch (JSONException jE) {
			jE.printStackTrace();
		}

		if ((responseCode >= HttpURLConnection.HTTP_OK)
				&& (responseCode < HttpURLConnection.HTTP_MULT_CHOICE)) {
			return gson.fromJson(dataString, clazz);
		} else {
			// String message = null;
			// String errorCode = null;
			// try {
			// ResponseType responseObject = gson.fromJson(responseString,
			// ResponseType.class);
			// message = responseObject.getMessage();
			// errorCode = responseObject.getCode();
			//
			// if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
			// JSONObject jsonResponse = new JSONObject(response);
			// if (dataJsonKey != null && jsonResponse.has(dataJsonKey)) {
			// // Bad request however need to check payload.
			// dataString = jsonResponse.getString(dataJsonKey);
			// if (dataString != null && !dataString.isEmpty()) {
			// return gson.fromJson(dataString, clazz);
			// }
			// }
			// }
			// if (WebServiceController.HTTP_RESPONSE_SESSION_EXPIRED ==
			// responseCode) {
			// throw new Exception(USER_CREDENTIALS_INVALID);
			// }
			// } catch (Exception e) {
			// e.printStackTrace();
			// if (WebServiceController.HTTP_RESPONSE_SESSION_EXPIRED ==
			// responseCode) {
			// throw new Exception(USER_CREDENTIALS_INVALID);
			// } else {
			// throw new Exception(WebServiceController.GENERIC_ERROR_MESSAGE);
			// }
			// }
			//
			// if ((message != null) && (message.trim().length() > 0)) {
			// if (ResponseType.class == clazz) {
			// throw new Exception(message);
			// } else {
			// throw new Exception(message);
			// }
			// } else {
			// throw new Exception(WebServiceController.GENERIC_ERROR_MESSAGE);
			// }
		}
		return null;
	}

	// public DeviceCredential deviceRegister(String pushId,
	// boolean isPushEnabled, String mobileOS, String mobileOSVer,
	// String mobileNumber, String appVer, String area,
	// Filter filter) {
	public DeviceCredential deviceRegister(
			DeviceRegisterRequest deviceRegisterRequest) {

		String url = URL_BASE + URL_DEVICE_REGISTER;
		int serverResponse = HttpURLConnection.HTTP_OK;
		StringBuffer responseBuffer = new StringBuffer();
		String params = gson.toJson(deviceRegisterRequest);

		try {
			serverResponse = this.httpClientManager.connectToWebServer(url,
					HttpClientManager.HTTP_METHOD.POST, params, responseBuffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return processServerResponse(serverResponse, responseBuffer.toString(),
				DeviceCredential.class, DeviceCredential.JSON_KEY);
	}

	public BasicResponse deviceSignIn(DeviceCredential deviceCredential) {

		String url = URL_BASE + URL_DEVICE_SIGN_IN;
		int serverResponse = HttpURLConnection.HTTP_OK;
		StringBuffer responseBuffer = new StringBuffer();
		String params = gson.toJson(deviceCredential);

		try {
			serverResponse = this.httpClientManager.connectToWebServer(url,
					HttpClientManager.HTTP_METHOD.POST, params, responseBuffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return processServerResponse(serverResponse, responseBuffer.toString(),
				BasicResponse.class, BasicResponse.JSON_KEY);
	}

	public DeviceSettings getDeviceSettings(String deviceId) {

		String url = URL_BASE
				+ URL_DEVICE_SETTINGS.replace("{deviceId}", deviceId);
		int serverResponse = HttpURLConnection.HTTP_OK;
		StringBuffer responseBuffer = new StringBuffer();

		try {
			serverResponse = this.httpClientManager.connectToWebServer(url,
					HttpClientManager.HTTP_METHOD.GET, null, responseBuffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return processServerResponse(serverResponse, responseBuffer.toString(),
				DeviceSettings.class, DeviceSettings.JSON_KEY);
	}

	public BasicResponse postDeviceSettings(String deviceId,
			DeviceSettings deviceSettings) {

		String url = URL_BASE
				+ URL_DEVICE_SETTINGS.replace("{deviceId}", deviceId);
		int serverResponse = HttpURLConnection.HTTP_OK;
		StringBuffer responseBuffer = new StringBuffer();
		String params = gson.toJson(deviceSettings);

		try {
			serverResponse = this.httpClientManager.connectToWebServer(url,
					HttpClientManager.HTTP_METHOD.POST, params, responseBuffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return processServerResponse(serverResponse, responseBuffer.toString(),
				BasicResponse.class, BasicResponse.JSON_KEY);
	}

	public List<Deal> getDeals(String filter) {

//		String url = URL_BASE + URL_GET_DEALS.replace("{filter}", filter);
//		int serverResponse = HttpURLConnection.HTTP_OK;
//		StringBuffer responseBuffer = new StringBuffer();
//
//		try {
//			serverResponse = this.httpClientManager.connectToWebServer(url,
//					HttpClientManager.HTTP_METHOD.GET, null, responseBuffer);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		List<Deal> dealList = new ArrayList<Deal>();
//		String responseString = responseBuffer.toString();
//		Deal[] deals = processServerResponse(serverResponse, responseString,
//				Deal[].class, Deal.JSON_KEY);
//		dealList = (deals != null) ? new ArrayList<Deal>(Arrays.asList(deals))
//				: new ArrayList<Deal>();
		
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("res/raw/sample_deal.txt");
		List<Deal> dealList = new ArrayList<Deal>();
		try {
			dealList = readJsonStream(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return dealList;
	}
	
	public List<Deal> readJsonStream(InputStream in) throws IOException {
		// TODO: increase buffer automatically
		byte[] buffer = new byte[1000000];
		int readBytes = in.read(buffer);
		String jsonString = new String(buffer, 0, readBytes);
		List<Deal> deals = new ArrayList<Deal>();
		Gson gson = new Gson();
		deals = Arrays.asList(gson.fromJson(jsonString, Deal[].class));
		
		return deals;
	}
}
