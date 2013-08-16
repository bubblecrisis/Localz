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

public class DeviceRegisterRequest {
	
	private Notification notification;
	private String mobileOS;
	private String mobileOSVer;
	private String mobileNumber;
	private String appVer;
	private String area;
	private Filter filter;

	public DeviceRegisterRequest(Notification notification, String mobileOS,
			String mobileOSVer, String mobileNumber, String apVer, String area,
			Filter filter) {
		super();
		this.notification = notification;
		this.mobileOS = mobileOS;
		this.mobileOSVer = mobileOSVer;
		this.mobileNumber = mobileNumber;
		this.appVer = apVer;
		this.area = area;
		this.filter = filter;
	}

	public Notification getNotification() {
		return notification;
	}

	public void setNotification(Notification notification) {
		this.notification = notification;
	}

	public String getMobileOS() {
		return mobileOS;
	}

	public void setMobileOS(String mobileOS) {
		this.mobileOS = mobileOS;
	}

	public String getMobileOSVer() {
		return mobileOSVer;
	}

	public void setMobileOSVer(String mobileOSVer) {
		this.mobileOSVer = mobileOSVer;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getApVer() {
		return appVer;
	}

	public void setApVer(String apVer) {
		this.appVer = apVer;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public Filter getFilter() {
		return filter;
	}

	public void setFilter(Filter filter) {
		this.filter = filter;
	}
}
