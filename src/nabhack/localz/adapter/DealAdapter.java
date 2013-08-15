package nabhack.localz.adapter;

import java.util.List;

import nabhack.localz.R;
import nabhack.localz.models.Deal;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

public class DealAdapter extends ArrayAdapter<Deal> {
	protected List<Deal> deals;
	protected int itemViewId;
	private Context ctx;

	public DealAdapter(Context context, int resource, int textViewResourceId,
			List<Deal> deals) {
		super(context, resource, textViewResourceId, deals);
		this.deals = deals;
		this.itemViewId = resource;
		this.ctx = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		DealListItem dealItem;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(itemViewId, null);
			dealItem = new DealListItem(v);
		} else {
			dealItem = (DealListItem) v.getTag();
		}

		final Deal deal = deals.get(position);
		if (deal != null) {

			// Uncomment when data will be available online
			// ImageLoader.getInstance().displayImage(deal.getDescImgs()[0],
			// dealItem.image);

			// Comment next block when data available online
			String uri = "drawable/"
					+ deal.getDescImgs()[0].replaceFirst("[.][^.]+$", "");
			int imageResource = ctx.getResources().getIdentifier(uri, null,
					ctx.getPackageName());
			Drawable image = ctx.getResources().getDrawable(imageResource);
			dealItem.image.setImageDrawable(image);

			dealItem.title.setText(deal.getTitle());
			dealItem.remaining.setText(deal.getQuantityLimit() + " Remaining"); // TODO:
																				// Include
																				// time
																				// remaining
		}
		return v;
	}

	public static class DealListItem {

		public ImageView image;
		public TextView title;
		public TextView remaining;

		public DealListItem(View v) {
			super();
			this.image = (ImageView) v.findViewById(R.id.image);
			this.title = (TextView) v.findViewById(R.id.title);
			this.remaining = (TextView) v.findViewById(R.id.remaining);
			v.setTag(this);
		}
	}
}