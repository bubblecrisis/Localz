package nabhack.localz.activity;

import java.util.ArrayList;
import java.util.List;

import nabhack.localz.LocalzApp;
import nabhack.localz.R;
import nabhack.localz.models.Category;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.internal.ca;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_filter)
public class FilterActivity extends Activity {
	FilterAdapter dataAdapter = null;

	@ViewById(R.id.filter_list_view)
	ListView filterListView;

	@AfterViews
	void initView() {
		List<Category> categories = ((LocalzApp) getApplication())
				.getCategories();
		filterListView.setAdapter(new FilterAdapter(this, R.layout.filter_item,
				categories));
	}

	@Click(R.id.filter_submit) 
	void done() {
		setResult(Activity.RESULT_OK);
		finish();
	}
	private class FilterAdapter extends ArrayAdapter<Category> {

		private ArrayList<Category> categoriesList;

		public FilterAdapter(Context context, int textViewResourceId,
				List<Category> categoriesList) {
			super(context, textViewResourceId, categoriesList);
			this.categoriesList = new ArrayList<Category>();
			this.categoriesList.addAll(categoriesList);
		}

		private class ViewHolder {
			TextView text;
			CheckBox cBox;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;
			Log.v("ConvertView", String.valueOf(position));

			if (convertView == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = vi.inflate(R.layout.filter_item, null);

				holder = new ViewHolder();
				holder.text = (TextView) convertView
						.findViewById(R.id.category);
				holder.cBox = (CheckBox) convertView
						.findViewById(R.id.checkBox);
				convertView.setTag(holder);

				holder.cBox.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						CheckBox cb = (CheckBox) v;
						Category category = (Category) cb.getTag();
						/*Toast.makeText(
								getApplicationContext(),
								"Clicked on Checkbox: " + cb.getText() + " is "
										+ cb.isChecked(), Toast.LENGTH_LONG)
								.show();*/
						category.setChecked(cb.isChecked());
					}
				});
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			Category category = categoriesList.get(position);
			holder.text.setText(category.getDealCategory());
			holder.cBox.setChecked(category.isChecked());
			holder.cBox.setTag(category);

			return convertView;

		}

	}

}
