package nabhack.localz.models;

/**
{
  code:0,
  data:{
    deviceId:"12sd234s234239832",     
    deviceKey:"1238sdf89234sdx2348sd9df3489d80324112489df82349f34kjlkjsdflkkj32l4jksdfjlkasrj23i4u908vlkxcvnw4hoxvc23730498diofhkan34lku341098sdifjenr23;i4uu09s8irw3j4alkfjaoiu234jsf97a9d8vaw4kllfa9sdv09080823ulajsflasj" 
  }
  message:""
}
 */
public class DeviceCredential {
	
	public static final String JSON_KEY = "data";

	private String deviceId;
	private String deviceKey;
	
	public DeviceCredential(String deviceId, String deviceKey) {
		super();
		this.deviceId = deviceId;
		this.deviceKey = deviceKey;
	}
	
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getDeviceKey() {
		return deviceKey;
	}
	public void setDeviceKey(String deviceKey) {
		this.deviceKey = deviceKey;
	}
}
