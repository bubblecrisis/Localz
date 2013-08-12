package nabhack.localz.activity;

import nabhack.localz.LocalzApp;
import nabhack.localz.R;
import nabhack.localz.adapter.DealAdapter;
import nabhack.localz.models.Deal;
import android.app.ActionBar;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ItemClick;
import com.googlecode.androidannotations.annotations.ViewById;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

@EActivity(R.layout.activity_deal_summary)
public class DealSummaryActivity extends FragmentActivity {

	static final float FADE_DEGREE = 0.35f;

	@ViewById
	ListView listView;

	@App
	LocalzApp application;
	
	SlidingMenu menu;

	SideMenuListFragment sideMenuFragment;

	
	@AfterViews
    void setupView() {
		listView.setAdapter(new DealAdapter(this, R.layout.deal_list_item, R.id.title, application.getDealsOnOffer()));  
		initSideMenu();
		initMenuOPtions();
    }
	
	private void initSideMenu() {
		// configure the SlidingMenu
		menu = new SlidingMenu(this);
		menu.setMode(SlidingMenu.LEFT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		menu.setShadowWidthRes(R.dimen.side_menu_shadow_width);
		menu.setShadowDrawable(R.drawable.sidemenu_shadow);
		menu.setBehindOffsetRes(R.dimen.side_menu_offset);
		menu.setFadeDegree(FADE_DEGREE);
		menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
		menu.setMenu(R.layout.menu_frame);
		menu.setOnClosedListener(new SlidingMenu.OnClosedListener() {
			@Override
			public void onClosed() {
		
			}
		});
		sideMenuFragment = new SideMenuListFragment_();
		
		getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, sideMenuFragment).commit();
		menu.setOnOpenListener(new SlidingMenu.OnOpenListener() {
			@Override
			public void onOpen() {
				sideMenuFragment.refresh();
			}
		});
	
	}
	
	@ItemClick(R.id.listView)
	void listViewClicked(int position) {
		application.setCurrentDeal((Deal) listView.getItemAtPosition(position));
		Intent intent = new Intent(this, DealDetailsActivity_.class);
		startActivity(intent);
	}
	
	void initMenuOPtions() {
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getActionBar().setCustomView(R.layout.abs_home_layout);
		ImageView menuIcon = (ImageView) findViewById(R.id.abs_home_menu_id);
		menuIcon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				menu.toggle();
			}
		});
	}
}
