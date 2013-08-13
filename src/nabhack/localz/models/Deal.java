package nabhack.localz.models;
/**
 * 	Deal:
	{
	store:{type:Schema.Types.ObjectId,ref:Store,required:true},
	user:{type:Schema.Types.ObjectId,ref:User,required:true},
	device:{type:Schema.Types.ObjectId,ref:Device,required:true},
	description:{type:String,required:true},
	descImgs:[{type:String}],
	expiryDateTime:{type:Date},
	status:{type:String,required:true,enum:['draft','published','complete','cancelled','suspended']},
	isLimitedTime:{type:Boolean},
	isLimitedQuantity:{type:Boolean},
	quantityLimit:{type:Number},
	isCheckinDeal:{type:Boolean},
	isPushDeal:{type:Boolean},
	discountCode:{type:String}
	altClaimLocation:{lat:{type:Number},lng:{type:Number}}
	activities:[
	{
	datetime:{type:Date},
	                activity:{type:String,enum:['claimed','push']},
	  	 user:{type:Schema.Types.ObjectId,ref:User},
	  	 device:{type:Schema.Types.ObjectId,ref:Device},
	  	 location:{lat:{type:Number},lng:{type:Number}},
	  	 ipAddress:{type:String}
	}]
 * @author hongyew
 *
 */
public class Deal {
	
	public static enum Status {
		draft, published, complete, cancelled, suspended;
	}
	
	private String _id;

	private long store;
	private long user;
	private long device;
	private String title;
	private String savings;
	private String cost;
	private String description;
	private String[] descImgs;
	private Status status;
	private boolean isLimitedTime;
	private boolean isLimitedQuantity;
	private int quantityLimit;
	private boolean isCheckinDeal;
	private boolean isPushDeal;
	private String discountCode;
	private Location altClaimLocation;
	
	public Deal(String title, String savings, String image, String description) {
		this.descImgs = new String[] { image };
		this.title = title;
		this.savings = savings;
		this.description = description;
	}
	
	public Deal(String title, String savings, String image, String description, float lat, float lng) {
		this.descImgs = new String[] { image };
		this.title = title;
		this.savings = savings;
		this.description = description;
		this.altClaimLocation = new Location(lat, lng);
	}
	
	public String toString() {
		return title;
	}
	
	/**
	 * @return the _id
	 */
	public String get_id() {
		return _id;
	}
	
	/**
	 * @return the store
	 */
	public long getStore() {
		return store;
	}
	/**
	 * @param store the store to set
	 */
	public void setStore(long store) {
		this.store = store;
	}
	/**
	 * @return the user
	 */
	public long getUser() {
		return user;
	}
	/**
	 * @param user the user to set
	 */
	public void setUser(long user) {
		this.user = user;
	}
	/**
	 * @return the device
	 */
	public long getDevice() {
		return device;
	}
	/**
	 * @param device the device to set
	 */
	public void setDevice(long device) {
		this.device = device;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the descImgs
	 */
	public String[] getDescImgs() {
		return descImgs;
	}
	/**
	 * @param descImgs the descImgs to set
	 */
	public void setDescImgs(String[] descImgs) {
		this.descImgs = descImgs;
	}
	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(Status status) {
		this.status = status;
	}
	/**
	 * @return the isLimitedTime
	 */
	public boolean isLimitedTime() {
		return isLimitedTime;
	}
	/**
	 * @param isLimitedTime the isLimitedTime to set
	 */
	public void setLimitedTime(boolean isLimitedTime) {
		this.isLimitedTime = isLimitedTime;
	}
	/**
	 * @return the isLimitedQuantity
	 */
	public boolean isLimitedQuantity() {
		return isLimitedQuantity;
	}
	/**
	 * @param isLimitedQuantity the isLimitedQuantity to set
	 */
	public void setLimitedQuantity(boolean isLimitedQuantity) {
		this.isLimitedQuantity = isLimitedQuantity;
	}
	/**
	 * @return the quantityLimit
	 */
	public int getQuantityLimit() {
		return quantityLimit;
	}
	/**
	 * @param quantityLimit the quantityLimit to set
	 */
	public void setQuantityLimit(int quantityLimit) {
		this.quantityLimit = quantityLimit;
	}
	/**
	 * @return the isCheckinDeal
	 */
	public boolean isCheckinDeal() {
		return isCheckinDeal;
	}
	/**
	 * @param isCheckinDeal the isCheckinDeal to set
	 */
	public void setCheckinDeal(boolean isCheckinDeal) {
		this.isCheckinDeal = isCheckinDeal;
	}
	/**
	 * @return the isPushDeal
	 */
	public boolean isPushDeal() {
		return isPushDeal;
	}
	/**
	 * @param isPushDeal the isPushDeal to set
	 */
	public void setPushDeal(boolean isPushDeal) {
		this.isPushDeal = isPushDeal;
	}
	/**
	 * @return the discountCode
	 */
	public String getDiscountCode() {
		return discountCode;
	}
	/**
	 * @param discountCode the discountCode to set
	 */
	public void setDiscountCode(String discountCode) {
		this.discountCode = discountCode;
	}
	/**
	 * @return the altClaimLocation
	 */
	public Location getAltClaimLocation() {
		return altClaimLocation;
	}
	/**
	 * @param altClaimLocation the altClaimLocation to set
	 */
	public void setAltClaimLocation(Location altClaimLocation) {
		this.altClaimLocation = altClaimLocation;
	}
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the savings
	 */
	public String getSavings() {
		return savings;
	}
	/**
	 * @param savings the savings to set
	 */
	public void setSavings(String savings) {
		this.savings = savings;
	}
	/**
	 * @return the cost
	 */
	public String getCost() {
		return cost;
	}
	/**
	 * @param cost the cost to set
	 */
	public void setCost(String cost) {
		this.cost = cost;
	}
	
	// User user
	
}
