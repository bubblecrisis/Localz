package nabhack.localz.models;

import java.util.ArrayList;

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

public class Filter {
	
	public static final String JSON_KEY = "filter";

	private ArrayList<String> categories;
	private ArrayList<String> stores;
	
	public Filter(ArrayList<String> categories, ArrayList<String> stores) {
		super();
		this.categories = categories;
		this.stores = stores;
	}
	
	public ArrayList<String> getCategories() {
		return categories;
	}
	
	public void setCategories(ArrayList<String> categories) {
		this.categories = categories;
	}
	
	public ArrayList<String> getStores() {
		return stores;
	}
	
	public void setStores(ArrayList<String> stores) {
		this.stores = stores;
	}	
}
