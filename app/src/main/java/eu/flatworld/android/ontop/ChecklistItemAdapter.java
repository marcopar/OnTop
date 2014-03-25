package eu.flatworld.android.ontop;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import eu.flatworld.android.ontop.db.ChecklistItem;

public class ChecklistItemAdapter extends ArrayAdapter<ChecklistItem> {
    public ChecklistItemAdapter(Context context, List<ChecklistItem> objects) {
        super(context, R.layout.checklistitem, R.id.tvText, objects);
    }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
        if (v == null) {
            v = super.getView(position, convertView, parent);
        }
        ChecklistItem ci = getItem(position);
        TextView tv = (TextView) v.findViewById(R.id.tvText);
        tv.setText(ci.getName());
        ImageView iv = (ImageView) v.findViewById(R.id.ivImage);
        if (ci.isChecked() == true) {
			iv.setImageResource(R.drawable.ic_check);
		} else {
			iv.setImageResource(R.drawable.ic_empty);
		}
        return v;
	}
}
