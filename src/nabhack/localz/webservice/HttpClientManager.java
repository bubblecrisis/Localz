package nabhack.localz.webservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerPNames;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

public class HttpClientManager {

	private static final String ACCEPT = "Accept";

	private static final String APPLICATION_JSON = "application/json";

	/**
	 * Enum for HTTP supported methods.
	 * 
	 * @author
	 * 
	 */
	public static enum HTTP_METHOD {
		GET, PUT, POST, DELETE
	};

	private static final int CONNECTION_TIMEOUT = 10000;
	private static final int SOCKET_TIMEOUT = 20000;
	private static final int CONN_PER_ROUTE_BEAN = 20;
	public DefaultHttpClient httpClient;

	public HttpClientManager() {
		httpClient = new DefaultHttpClient();
		ClientConnectionManager mgr = httpClient.getConnectionManager();
		HttpParams params = httpClient.getParams();
		params.setParameter(ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE,
				new ConnPerRouteBean(HttpClientManager.CONN_PER_ROUTE_BEAN));
		HttpConnectionParams.setConnectionTimeout(params,
				HttpClientManager.CONNECTION_TIMEOUT);
		HttpConnectionParams.setSoTimeout(params,
				HttpClientManager.SOCKET_TIMEOUT);
		ThreadSafeClientConnManager threadSafeClientConnManager = new ThreadSafeClientConnManager(
				params, mgr.getSchemeRegistry());
		httpClient = new DefaultHttpClient(threadSafeClientConnManager, params);
	}

	public void destroyWebServerPubisher() {
		httpClient.getConnectionManager().shutdown();
	}

	public int connectToWebServer(String url,
			HttpClientManager.HTTP_METHOD httpMethod, String params,
			StringBuffer responseBuffer) throws IOException {

		int returnValue = HttpStatus.SC_INTERNAL_SERVER_ERROR;
		InputStream isContent = null;
		HttpRequestBase request = null;
		StringEntity se = (params != null) ? new StringEntity(params)
				: new StringEntity("");

		if (isDebug()) {
			System.out.println("Request url " + url + " params" + params);
		}

		if (url.trim().length() > 0) {
			switch (httpMethod) {
			case GET:
				request = new HttpGet(url);
				request.addHeader(new BasicHeader(ACCEPT, APPLICATION_JSON));
				break;
			case PUT:
				se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
						APPLICATION_JSON));
				request = new HttpPut(url);
				((HttpEntityEnclosingRequestBase) request).setEntity(se);
				break;
			case POST:
				se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
						APPLICATION_JSON));
				request = new HttpPost(url);
				((HttpEntityEnclosingRequestBase) request).setEntity(se);
				break;
			case DELETE:
				request = new HttpDelete(url);
				request.addHeader(new BasicHeader(ACCEPT, APPLICATION_JSON));
				break;
			default:
				break;
			}

			try {
				HttpResponse httpResponse = httpClient.execute(request);
				if (isDebug()) {
					Header[] allHeaders = httpResponse.getAllHeaders();
					for (Header h : allHeaders) {
						System.out.println("HEADERS: " + h.getName()
								+ " value: " + h.getValue());
					}
				}
				returnValue = httpResponse.getStatusLine().getStatusCode();
				isContent = httpResponse.getEntity().getContent();
				BufferedReader buffer = new BufferedReader(
						new InputStreamReader(isContent));
				String s = "";

				while ((s = buffer.readLine()) != null) {
					responseBuffer.append(s);
				}
			} finally {
				try {
					if (isContent != null) {
						isContent.close();
					}
				} catch (IOException e) {
					// do nothing
				}
			}
		}

		if (isDebug()) {
			System.out.println("Response code " + returnValue + " buffer "
					+ responseBuffer);
		}
		return returnValue;
	}

	private boolean isDebug() {
		return false;
	}
}
