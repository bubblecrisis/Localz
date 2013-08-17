package nabhack.localz.models;

/**
{
  code:0
  data:{
     success:true
  }
}
 */
public class BasicResponse {

	public static final String JSON_KEY = "data";

	private boolean success;

	public BasicResponse(boolean success) {
		super();
		this.success = success;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public static String getJsonKey() {
		return JSON_KEY;
	}
}
