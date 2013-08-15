package nabhack.localz.models;

public class Category {
	String dealCategory;
	boolean isChecked;
	public Category(String dealCategory, boolean isChecked) {
		super();
		this.dealCategory = dealCategory;
		this.isChecked = isChecked;
	}
	public String getDealCategory() {
		return dealCategory;
	}
	public void setDealCategory(String dealCategory) {
		this.dealCategory = dealCategory;
	}
	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
}
