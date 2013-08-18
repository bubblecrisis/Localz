package nabhack.localz.adapter;

import java.util.HashMap;
import java.util.List;

import nabhack.localz.R;
import nabhack.localz.models.Deal;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DealAdapter extends ArrayAdapter<Deal> {
	protected List<Deal> deals;
	protected int itemViewId;
	private Context ctx;
	private HashMap<Integer, Boolean> timerMap = new HashMap<Integer, Boolean>();

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
		//*** Hack *** Turn off view recycling as it fixes the timers.
		//if (v == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(itemViewId, null);
			dealItem = new DealListItem(v);
		//} else {
		//	dealItem = (DealListItem) v.getTag();
		//}

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

			if (deal.getQuantityLimit() == 0) {
				dealItem.remaining.setVisibility(View.GONE);
			} else {
				dealItem.remaining.setText(deal.getQuantityLimit()
						+ " Remaining");
				dealItem.remaining.setVisibility(View.VISIBLE);
			}
			dealItem.shortDesc.setText(deal.getShortDescription());
			if (deal.getSecondsToExpire() == 0) {
				dealItem.time.setVisibility(View.GONE);
			} else {
				dealItem.time.setVisibility(View.VISIBLE);
				dealItem.time.setText(getTimeFormat(deal.getSecondsToExpire()));
				
				if (timerMap.get(deal.getId()) == null) {
					timerMap.put(new Integer(deal.getId()), Boolean.valueOf(true));
					timerCountDown(deal.getSecondsToExpire(), dealItem.time);						
				} 
			}
		}

		return v;
	}
	
	public void timerCountDown(final int secondCount, final TextView textView) {
		
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {
				if (secondCount > 0) {
					// format time and display
					textView.setText(getTimeFormat(secondCount));
					timerCountDown(secondCount - 1, textView);
				} else {

				}
			}
		}, 1000);
	}

	public static class DealListItem {

		public ImageView image;
		public TextView title;
		public TextView remaining;
		public TextView shortDesc;
		public TextView time;

		public DealListItem(View v) {
			super();
			this.image = (ImageView) v.findViewById(R.id.image);
			this.title = (TextView) v.findViewById(R.id.title);
			this.remaining = (TextView) v.findViewById(R.id.remaining);
			this.shortDesc = (TextView) v.findViewById(R.id.special);
			this.time = (TextView) v.findViewById(R.id.time);
			v.setTag(this);
		}
	}

	private String getTimeFormat(int secs) {
		int hours = secs / 3600, remainder = secs % 3600, minutes = remainder / 60, seconds = remainder % 60;

		String disHour = (hours < 10 ? "0" : "") + hours, disMinu = (minutes < 10 ? "0"
				: "")
				+ minutes, disSec = (seconds < 10 ? "0" : "") + seconds;
		return disHour + ":" + disMinu + ":" + disSec;
	}
}