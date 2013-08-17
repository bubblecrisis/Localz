package nabhack.localz.models;

/** 
/register
 
{
    "notification": {
        "pushId": "a123asdfj234ubsd323s",
        "isPushEnabled": true
    },
    "mobileOS": "Android",
    "mobileOSVer": "4.3.1",
    "mobileNumber": "0412312312",
    "appVer": "0.1",
    "area": "520a01e1c5b56578f6000008",
    "filter": {
        "categories": [
            "Toys",
            "Food"
        ],
        "stores": [
            "520a01e1c5b56578f6000008",
            "520a01e1c5b56578f6000008",
            "520a01e1c5b56578f6000008"
        ]
    }
}
 */

public class Notification {
	
	public static final String JSON_KEY = "notification";

	private String pushId;
	private boolean isPushEnabled;

	public Notification(String pushId, boolean isPushEnabled) {
		super();
		this.pushId = pushId;
		this.isPushEnabled = isPushEnabled;
	}

	public String getPushId() {
		return pushId;
	}

	public void setPushId(String pushId) {
		this.pushId = pushId;
	}

	public boolean isPushEnabled() {
		return isPushEnabled;
	}

	public void setPushEnabled(boolean isPushEnabled) {
		this.isPushEnabled = isPushEnabled;
	}
}