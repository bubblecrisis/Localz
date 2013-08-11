package nabhack.localz.adaptor;

import java.util.List;

import nabhack.localz.R;
import nabhack.localz.models.Deal;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DealAdaptor extends ArrayAdapter<Deal> {
	protected List<Deal> deals;
	protected int itemViewId;
	
	public DealAdaptor(Context context, int resource,
			int textViewResourceId, List<Deal> deals) {
		super(context, resource, textViewResourceId, deals);
		this.deals = deals;
		this.itemViewId = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		DealListItem dealItem;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(itemViewId, null);
			dealItem = new DealListItem(v);
		} else {
			dealItem = (DealListItem) v.getTag();
		}

		final Deal deal = deals.get(position);
		if (deal != null) {
			dealItem.image.loadUrl(deal.getDescImgs()[0]);
			dealItem.title.setText(deal.getTitle());
			dealItem.remaining.setText(deal.getQuantityLimit() + " Remaining"); // TODO: Include time remaining
		}
		return v;
	}
	
	public static class DealListItem {

		public WebView image;
		public TextView title;
		public TextView remaining;
		
		public DealListItem(View v) {
			super();
			this.image = (WebView) v.findViewById(R.id.image);
			this.title = (TextView) v.findViewById(R.id.title);
			this.remaining = (TextView) v.findViewById(R.id.remaining);
			v.setTag(this);
		}
	}
}