package nabhack.localz.models;

import com.google.gson.annotations.Expose;

/**
 * Deal: { store:{type:Schema.Types.ObjectId,ref:Store,required:true},
 * user:{type:Schema.Types.ObjectId,ref:User,required:true},
 * device:{type:Schema.Types.ObjectId,ref:Device,required:true},
 * description:{type:String,required:true}, descImgs:[{type:String}],
 * expiryDateTime:{type:Date}, status:{type:String,required:true,enum:[
 * 'draft','published','complete','cancelled','suspended']},
 * isLimitedTime:{type:Boolean}, isLimitedQuantity:{type:Boolean},
 * quantityLimit:{type:Number}, isCheckinDeal:{type:Boolean},
 * isPushDeal:{type:Boolean}, discountCode:{type:String}
 * altClaimLocation:{lat:{type:Number},lng:{type:Number}} activities:[ {
 * datetime:{type:Date}, activity:{type:String,enum:['claimed','push']},
 * user:{type:Schema.Types.ObjectId,ref:User},
 * device:{type:Schema.Types.ObjectId,ref:Device},
 * location:{lat:{type:Number},lng:{type:Number}}, ipAddress:{type:String} }]
 * 
 * { store:{ _id:new mongoose.Types.ObjectId, name:"JB Hifi" },
 * categories:["Electronics, Entetainment"],
 * title:"75% off all movies next 20 customers", savings:"", cost:"",
 * description:
 * "75% off of any regular priced DVD or Blu-Ray movie. Applys to any in-stock movie.  Must be claimed by 6pm 31 August 2013. Not valid with any other offers"
 * , descImgs:["dealImages/jbhifi.gif"], location:{lat:-37.884795,
 * lng:145.082812}, expiryDateTime: "2014-08-21 18:00:00TZ",
 * isLimitedTime:false, isLimitedQuantity:true, quantityLimit:20,
 * availableQuantity:20, isCheckinDeal:false, isPushDeal:true,
 * isSecureable:true, isUserRequired:false, discountCode:"Movie123ABC" }
 * 
 * @author hongyew
 * 
 */
public class Deal {

	public static enum Status {
		draft, published, complete, cancelled, suspended;
	}
	@Expose
	private String id;
	@Expose
	private Store store;
	@Expose
	private String[] categories;
	@Expose
	private String title;
	@Expose
	private String savings;
	@Expose
	private String cost;
	@Expose
	private String description;
	@Expose
	private String[] descImgs;
	@Expose
	private Location location;
	@Expose
	private String expiryDateTime;
	@Expose
	private boolean isLimitedTime;
	@Expose
	private boolean isLimitedQuantity;
	@Expose
	private int quantityLimit;
	@Expose
	private int availableQuantity;
	@Expose 
	private boolean isCheckinDeal;
	@Expose
	private boolean isPushDeal;
	@Expose
	private boolean isSecureable;
	@Expose
	private boolean isUserRequired;
	@Expose
	private String discountCode;
	public Store getStore() {
		return store;
	}
	public void setStore(Store store) {
		this.store = store;
	}
	public String[] getCategories() {
		return categories;
	}
	public void setCategories(String[] categories) {
		this.categories = categories;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSavings() {
		return savings;
	}
	public void setSavings(String savings) {
		this.savings = savings;
	}
	public String getCost() {
		return cost;
	}
	public void setCost(String cost) {
		this.cost = cost;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String[] getDescImgs() {
		return descImgs;
	}
	public void setDescImgs(String[] descImgs) {
		this.descImgs = descImgs;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public String getExpiryDateTime() {
		return expiryDateTime;
	}
	public void setExpiryDateTime(String expiryDateTime) {
		this.expiryDateTime = expiryDateTime;
	}
	public boolean isLimitedTime() {
		return isLimitedTime;
	}
	public void setLimitedTime(boolean isLimitedTime) {
		this.isLimitedTime = isLimitedTime;
	}
	public boolean isLimitedQuantity() {
		return isLimitedQuantity;
	}
	public void setLimitedQuantity(boolean isLimitedQuantity) {
		this.isLimitedQuantity = isLimitedQuantity;
	}
	public int getQuantityLimit() {
		return quantityLimit;
	}
	public void setQuantityLimit(int quantityLimit) {
		this.quantityLimit = quantityLimit;
	}
	public int getAvailableQuantity() {
		return availableQuantity;
	}
	public void setAvailableQuantity(int availableQuantity) {
		this.availableQuantity = availableQuantity;
	}
	public boolean isCheckinDeal() {
		return isCheckinDeal;
	}
	public void setCheckinDeal(boolean isCheckinDeal) {
		this.isCheckinDeal = isCheckinDeal;
	}
	public boolean isPushDeal() {
		return isPushDeal;
	}
	public void setPushDeal(boolean isPushDeal) {
		this.isPushDeal = isPushDeal;
	}
	public boolean isSecureable() {
		return isSecureable;
	}
	public void setSecureable(boolean isSecureable) {
		this.isSecureable = isSecureable;
	}
	public boolean isUserRequired() {
		return isUserRequired;
	}
	public void setUserRequired(boolean isUserRequired) {
		this.isUserRequired = isUserRequired;
	}
	public String getDiscountCode() {
		return discountCode;
	}
	public void setDiscountCode(String discountCode) {
		this.discountCode = discountCode;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}


	

}
