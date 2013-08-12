package nabhack.localz.activity;

import nabhack.localz.R;
import android.support.v4.app.Fragment;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EFragment;

@EFragment(R.layout.fragment_side_menu_main)
public class SideMenuListFragment extends Fragment {

	@AfterViews
	void initDisplay() {
		//load list
	}
	
	public void refresh() {
		
	}
}