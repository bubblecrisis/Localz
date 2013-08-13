/*
 * (C) 2012 National Australia Bank
 *
 * [All rights reserved]. This product and related documentation are protected
 * by copyright restricting its use, copying, distribution, and decompilation.
 * No part of this product or related documentation may be reproduced in any
 * form by any means without prior written authorization of
 * National Australia Bank. Unless otherwise arranged, third parties may not
 * have access to this product or related documents.
 */

package nabhack.localz.adapter;

import java.util.ArrayList;
import java.util.List;

import nabhack.localz.LocalzApp;
import nabhack.localz.R;
import nabhack.localz.models.Deal;
import nabhack.localz.ui.compositelist.DealRow;
import nabhack.localz.ui.compositelist.Header;
import nabhack.localz.ui.compositelist.HeaderRow;
import nabhack.localz.ui.compositelist.Menu;
import nabhack.localz.ui.compositelist.Menu.MenuType;
import nabhack.localz.ui.compositelist.MenuRow;
import nabhack.localz.ui.compositelist.Row;
import nabhack.localz.ui.compositelist.RowType;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;

/**
 * This class represent data put into the side menu.
 * 
 * @author markng
 * 
 */
@EBean
public class SideMenuListAdapter extends BaseAdapter implements CompositeListAdapter {

	public static final String TAG = "MainSideMenuListAdapter";
	
	List<Row> rowData;

	@RootContext
	Context context;
	
	int staticItemCount;
	
	@App
	LocalzApp application;

	@AfterInject
	void initAdapter() {
		
		rowData = new ArrayList<Row>();
				
		Menu menu = new Menu(R.string.side_menu_settings_text, R.drawable.ic_notify_settings, MenuType.SETTINGS);
		rowData.add(new MenuRow(menu));
		
		menu = new Menu(R.string.side_menu_contact_nab_text, R.drawable.ic_notify_contact, MenuType.CONTACT_NAB);
		rowData.add(new MenuRow(menu));

		Menu menu3 = new Menu(R.string.side_menu_logout_text, R.drawable.ic_notify_logout, MenuType.LOGOUT, false);
		rowData.add(new MenuRow(menu3));
		
		HeaderRow headerRow = new HeaderRow(new Header(R.string.side_menu_header_text));
		rowData.add(headerRow);
		
		staticItemCount = 4;
		
		//TODO retrieved secured deals
		List<Deal> deals = application.getDealsOnOffer();
		for (Deal deal : deals) {
			DealRow dealRow = new DealRow(deal, true);
			rowData.add(dealRow);
		}
		
		notifyDataSetChanged();
	}
	
	/**
	 * @see android.widget.Adapter#getCount()
	 * @return number of items in the list
	 */
	public int getCount() {
		return rowData.size();
	}

	/**
	 * @see android.widget.Adapter#getItem(int)
	 * @param position
	 *            of item in the list to return
	 * @return Account item at the position in the list
	 */
	public Row getItem(int position) {
		return rowData.get(position);
	}

	/**
	 * @see android.widget.Adapter#getItemId(int)
	 * @param position
	 *            of item in the list to return
	 * @return postion of item
	 */
	public long getItemId(int position) {
		return position;
	}

	/**
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 * 
	 * @param position
	 *            postion in the list to build view for
	 * @param convertView
	 *            view of item
	 * @param parent
	 *            parent of ListView
	 * 
	 * @return View of account item populated with account data
	 */
	  public View getView(int position, View convertView, ViewGroup parent) {
          return rowData.get(position).getView(position, convertView, context);
      }
	  
      @Override
      public int getViewTypeCount() {
          return RowType.values().length;
      }

      @Override
      public int getItemViewType(int position) {
          return rowData.get(position).getViewType();
      }
      
      /**
       * Add new row to adapter.
       * @param row row to add.
       */
      public void add(Row row) {
    	  Log.d(TAG, "adding row");
    	  rowData.add(row);
    	  notifyDataSetChanged();
      }
      
      /**
  	 * Remove token and transaction from list.
  	 * 
  	 * @param index
  	 *            the index to delete.
  	 */
  	public void remove(int index) {
  		if (rowData.size() > index) {
  			rowData.remove(index);
  			notifyDataSetChanged();
  		}
  	}

  	/**
  	 * Remove all tokens and transactions from the list.
  	 */
  	public void removeAll() {
  		rowData.removeAll(rowData);
  		notifyDataSetChanged();
  	}

  	/**
  	 * Remove all tokens and transactions from the list.
  	 */
  	public void removeAllNonStaticItems() {
  		while (rowData.size() > staticItemCount) {
  			rowData.remove(rowData.size() - 1);
  		}
  		notifyDataSetChanged();
  	}
 	
}
