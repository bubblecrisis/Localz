package nabhack.localz.utils;

import nabhack.localz.application.Session;

/**
 * Convenient methods for dealing with Session.
 * 
 * @author rlay
 */
public final class SessionUtils {
	
	private SessionUtils() {
	}

	/**
	 * Return a boolean primitive session value.
	 * @param key The session key
	 * @return The boolean value
	 */
	public static boolean getBooleanValue(String key) {
		Boolean result = (Boolean) Session.getSession().get(key);
		return result == null ? false : result;
	}

  	/**
	 * Determine if the facebook photo displayed.
	 * @return True is the facebook photo is to be displayed
	 */
	public static boolean isFacebookPhotoDisplayed() {
		boolean isFacebookSet = getBooleanValue(Session.IS_FACEBOOK_SET);
		boolean isPhotoDisplayed = getBooleanValue(Session.IS_PHOTO_DISPLAYED);
		boolean isPhotoEnabled = getBooleanValue(Session.IS_PHOTO_ENABLE);
		return isFacebookSet && isPhotoDisplayed && isPhotoEnabled;
	}

	/**
	 * Sets the session facebook id.
	 * @param facebookId The users facebook id
	 */
	public static void setFacebookId(String facebookId) {
		Session.getSession().put(Session.USER_FACEBOOK_ID, facebookId);
	}
	
	/**
	 * Gets the session facebook id.
	 * @return The users facebook id
	 */
	public static String getFacebookId() {
		return (String) Session.getSession().get(Session.USER_FACEBOOK_ID);
	}
	
	/**
	 * Get the session facebook enabled setting.
	 * @return True if facebook is enabled
	 */
	public static boolean isFacebookEnabled() {
		return getBooleanValue(Session.IS_FACEBOOK_SET);
	}
	
	/**
	 * Sets the session facebook enabled setting.
	 * @param enabled True if facebook is enabled
	 */
	public static void setFacebookEnabled(boolean enabled) {
		Session.getSession().put(Session.IS_FACEBOOK_SET, Boolean.valueOf(enabled));
	}
	
	/**
	 * Get the session photo enabled setting.
	 * @return True if photos are enabled
	 */
	public static boolean isPhotoEnabled() {
		return getBooleanValue(Session.IS_PHOTO_ENABLE);
	}
}
