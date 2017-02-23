package dasudian.com.im.demo.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import dasudian.com.im.demo.Group;
import dasudian.com.im.demo.R;


public class GroupAdapter extends ArrayAdapter<Group> {

	private int resource;

	public GroupAdapter(Context context, int resource, List<Group> objects) {
		super(context, resource, objects);
		this.resource = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Group group = getItem(position);
		View view;
		ViewHolder viewHolder;
		if (convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(resource, null);
			viewHolder = new ViewHolder();
			viewHolder.groupName = (TextView) view
					.findViewById(R.id.tv_group_name);
			view.setTag(viewHolder);
		} else {
			view = convertView;
			viewHolder = (ViewHolder) view.getTag();
		}
		viewHolder.groupName.setText(group.getName());
		return view;

	}

	class ViewHolder {
		TextView groupName;
	}

}
