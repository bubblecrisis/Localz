package nabhack.localz.models;

/**
All response messages will be wrapped in
{
  code:<error code> - 0 is ok
  data:<the JSON payload>
  message:<Any messages>
}
 */

public class ResponseType {
	
    public static final String JSON_KEY = "message";
    
    private String code;
    private String message;
        
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}