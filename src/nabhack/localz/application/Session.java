package nabhack.localz.application;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Stores data for the users session.
 * 
 * @author gennadi
 *
 */
public final class Session {

	public static final String IS_FACEBOOK_SET = "isFacebookSet";
	public static final String IS_PHOTO_DISPLAYED = "isPhotoDisplayed";
	public static final String IS_PHOTO_ENABLE = "isPhotoEnable";
	public static final String USER_FACEBOOK_DATA = "userFacebookData";
	public static final String USER_FACEBOOK_ID = "userFacebookId";
	
	private static Map<String, Object> map;
	private static Session session;

	/**
	 * Private constructor, as this class is singleton.
	 */
	private Session() {
		map = new ConcurrentHashMap<String, Object>();
	}

	/**
	 * Get instance of Session, instantiate if it is null.
	 * 
	 * @return Session singleton.
	 */
	public static Session getSession() {
		if (Session.session == null) {
			session = new Session();
		}
		return Session.session;
	}

	/**
	 * This method destroys the sigleton instance.
	 */
	public static void destroySession() {
		if (Session.session != null) {
			session = null;
			map = null;
		}
	}

	/**
	 * Put object into the session.
	 * 
	 * @param attributeName the attribute name
	 * @param value the object
	 */
	public void put(String attributeName, Object value) {
		if (value == null) {
			map.remove(attributeName);
		} else {
			map.put(attributeName, value);
		}
	}

	/**
	 * Get object from the session.
	 * 
	 * @param attributeName the attribute name.
	 * @return the object.
	 */
	public Object get(String attributeName) {
		return map.get(attributeName);
	}
	
	
	/**
	 * Get object from the session.
	 * 
	 * @param attributeName the attribute name.
	 */
	public void remove(String attributeName) {
		map.remove(attributeName);
	}

}
