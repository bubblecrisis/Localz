package nabhack.localz.activity;

import java.util.ArrayList;
import java.util.List;

import nabhack.localz.R;
import nabhack.localz.utils.SessionUtils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.facebook.Session;
import com.facebook.SessionDefaultAudience;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.googlecode.androidannotations.annotations.EFragment;


/**
 * Fragment that manages the Facebook session. This fragment is intended to be an invisible fragment that runs in the background.
 * 
 * @author rlay
 */

@EFragment
public class FacebookFragment extends Fragment {

	private static final String TAG = FacebookFragment.class.getSimpleName();

	public static final String NAME = "name";
	public static final String ID = "id";
	public static final String EMAIL = "email";
	public static final String FIELDS = "fields";

	private UiLifecycleHelper uiHelper;
	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	private boolean isResumed;

	private IFacebookSessionCallback facebookCallback;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate()");
		uiHelper = new UiLifecycleHelper(getActivity(), callback);
		uiHelper.onCreate(savedInstanceState);
		facebookCallback.onFacebookFragmentReady(getActiveSession());
	}

	/**
	 * Callback to parent activity.
	 */
	public interface IFacebookSessionCallback {
		/**
		 * Callback when the facebook session state changes.
		 * 
		 * @param session
		 *            The active facebook session
		 * @param state
		 *            The session state
		 * @param exception
		 *            The exception, if any
		 */
		void onFacebookSessionStateChange(Session session, SessionState state, Exception exception);
		
		/**
		 * Callback when the facebook fragment is ready.
		 * 
		 * @param session
		 *            The active facebook session
		 */
		void onFacebookFragmentReady(Session session);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof IFacebookSessionCallback) {
			facebookCallback = (IFacebookSessionCallback) activity;
		} else {
			throw new ClassCastException(activity.toString() + " must implement " + FacebookFragment.class.getName() + "."
					+ IFacebookSessionCallback.class.getName());
		}
	}

	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		if (isResumed) {
			Log.d(TAG, "onSessionStateChange() state:" + state + " session:" + session + " id:" + this);
			if (exception != null) {
				Log.d(TAG, "onSessionStateChange() exception:" + exception.getMessage());
			}
			
			if (session != null && session.isClosed()) {
				SessionUtils.setFacebookEnabled(false);
			}
			
			facebookCallback.onFacebookSessionStateChange(session, state, exception);
		}
	}

	/**
	 * Presents the user with a login web dialog. It will not use the native Facebook app. Any existing facebook session will be
	 * overwritten.
	 */
	public void loginViaWebDialog() {
		Log.d(TAG, "loginViaWebDialog()");
		Session.OpenRequest openRequest = new Session.OpenRequest(this);
		openRequest.setDefaultAudience(SessionDefaultAudience.FRIENDS);
		List<String> permissions = new ArrayList<String>();
		permissions.add("publish_stream");
		openRequest.setPermissions(permissions);
		openRequest.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);
		String appId = getString(R.string.app_id);
		Log.d(TAG, "application id" + appId);
		Session session = new Session.Builder(getActivity()).setApplicationId(appId).build();
		Session.setActiveSession(session);
		session.openForPublish(openRequest);
	}

	/**
	 * Presents the user with a login web dialog. This will not ask for any permissions.
	 */
	public void loginViaWebDialogNoPermissions() {
		Log.d(TAG, "loginViaWebDialogNoPermissions()");
		Session.OpenRequest openRequest = new Session.OpenRequest(this);
		openRequest.setDefaultAudience(SessionDefaultAudience.FRIENDS);
		openRequest.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);
		Session session = new Session.Builder(getActivity()).setApplicationId(getString(R.string.app_id)).build();
		Session.setActiveSession(session);
		session.openForRead(openRequest);
	}

	/**
	 * Close any current active session and clears the token.
	 */
	public void logoff() {
		Log.d(TAG, "logoff()");
		Session session = Session.getActiveSession();
		if (session != null) {
			session.closeAndClearTokenInformation();
		}
	}

	/**
	 * Whether there is a current open session.
	 * 
	 * @return True if the session is open
	 */
	public boolean isSessionOpen() {
		Session session = Session.getActiveSession();
		if (session != null) {
			return session.isOpened();
		}
		return false;
	}

	@Override
	public void onResume() {
		super.onResume();
		uiHelper.onResume();
		isResumed = true;
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy()");
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	/**
	 * Get the active facebook session.
	 * 
	 * @return the session.
	 */
	public Session getActiveSession() {
		return Session.getActiveSession();
	}
}
