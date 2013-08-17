package nabhack.localz.models;

/**
GET /devices/<deviceId>

Retrieves device details and settings. If there is a user associated with this device, then user must be logged in before deviceId can be returned or 401 will be returned.

Response example:
{
  notification:{
    isPushEnabled:<true|false>
    pushID:"aasdjfasdfj39493808f0s8f",
  }, 
  filter:{
    categories:['limitedOffers','bigDiscounts','votes>100'],
    stores:[aasdjfasdfj39493808f0s8f, aasdjfasdfj39493808f0s8f]
  }
}
 */
public class DeviceSettings {

	public static final String JSON_KEY = "data";

	private Notification notification;
	private Filter filter;
	
	public DeviceSettings(Notification notification, Filter filter) {
		super();
		this.notification = notification;
		this.filter = filter;
	}
	
	public Notification getNotification() {
		return notification;
	}
	
	public void setNotification(Notification notification) {
		this.notification = notification;
	}
	
	public Filter getFilter() {
		return filter;
	}
	
	public void setFilter(Filter filter) {
		this.filter = filter;
	}
}
