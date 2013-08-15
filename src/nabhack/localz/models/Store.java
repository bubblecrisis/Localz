package nabhack.localz.models;

import com.google.gson.annotations.Expose;

/**
 *     store:{
         _id:new mongoose.Types.ObjectId,
        name:"JB Hifi"
    },
 * @author gennadi
 *
 */
public class Store {
	@Expose
	private String _id;
	@Expose
	private String name;
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
