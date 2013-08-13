package nabhack.localz.activity;

import nabhack.localz.R;
import nabhack.localz.adapter.SideMenuListAdapter;
import android.support.v4.app.Fragment;
import android.widget.ListView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;

@EFragment(R.layout.fragment_side_menu_main)
public class SideMenuListFragment extends Fragment {

	@Bean
    SideMenuListAdapter sideMenuListAdapter;
	
	@ViewById(R.id.list)
	ListView list;
	
	@AfterViews
	void initDisplay() {
		list.setAdapter(sideMenuListAdapter);
	}
	
	public void refresh() {
		
	}
}